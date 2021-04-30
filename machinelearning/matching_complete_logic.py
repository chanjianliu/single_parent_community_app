import pandas as pd

pd.set_option('display.max_colwidth', 500)
import firebase_admin
from firebase_admin import credentials, firestore

from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
from sklearn.cluster import KMeans, AgglomerativeClustering
from sklearn.metrics import silhouette_score, davies_bouldin_score
from sklearn.preprocessing import MinMaxScaler

#firebase_admin.initialize_app()

cred = credentials.Certificate('./FireStore.json')
firebase_admin.initialize_app(cred,
                              {
                                  'databaseURL': 'https://FireBaseTest.firebaseio.com/'
                              })


def matching_algo():
    # if not firebase_admin._apps:
    #     firebase_admin.initialize_app()

    db = firestore.client()

    df0 = pd.read_csv("test.csv")
    # print(df0['numberOfKids'])
    df0['combined_description'] = df0[['selfDescription', 'profession', 'interest']].apply(
        lambda x: ",".join(x.dropna().astype(str)), axis=1)
    df = df0.drop(['username', 'selfDescription', 'profession', 'interest'], axis=1)

    # Instantiating the Scaler
    scaler = MinMaxScaler()

    # print("***********",df.columns[0:1])
    # Scaling the categories then replacing the old values
    df = df[['combined_description']].join(
        pd.DataFrame(
            scaler.fit_transform(
                df.drop('combined_description', axis=1)),
            columns=df.columns[0:1],
            index=df.index))
    ##print("&&&&&&&&&&df&&&&&&&&&&&&",df)
    # Instantiating the Vectorizer, experimenting with both
    vectorizer = CountVectorizer()
    # vectorizer = TfidfVectorizer()

    # Fitting the vectorizer to the Bios
    x = vectorizer.fit_transform(df['combined_description'])

    # Creating a new DF that contains the vectorized words
    df_wrds = pd.DataFrame(x.toarray(), columns=vectorizer.get_feature_names())

    # Concating the words DF with the original DF
    new_df = pd.concat([df, df_wrds], axis=1)

    # Dropping the Bios because it is no longer needed in place of vectorization
    new_df.drop('combined_description', axis=1, inplace=True)
    # print("&&&&&&&&&&&&new_df&&&&&&&&&&",new_df)

    from sklearn.decomposition import PCA

    n_comp = 0
    expl_variance = 0

    while expl_variance < 0.90:
        n_comp += 1

        pca = PCA(n_components=n_comp)
        df_pca = pca.fit_transform(new_df)

        expl_variance = pca.explained_variance_ratio_.sum()

    print('Required', n_comp, 'principal component to capture', expl_variance, 'variance')
    ##Required 13 principal component to capture 0.9004184518783078 variance
    pca = PCA(n_components=n_comp)
    df_pca = pca.fit_transform(new_df)
    # print(df_pca)


    # Setting the amount of clusters to test out
    cluster_cnt = [i for i in range(2, 20, 1)]

    # Establishing empty lists to store the scores for the evaluation metrics
    s_scores = []

    db_scores = []

    # Looping through different iterations for the number of clusters
    for i in cluster_cnt:
        # Hierarchical Agglomerative Clustering with different number of clusters
        # hac = AgglomerativeClustering(n_clusters=i)

        # hac.fit(df_pca)

        # cluster_assignments = hac.labels_

        ## KMeans Clustering with different number of clusters
        k_means = KMeans(n_clusters=i)

        k_means.fit(df_pca)

        cluster_assignments = k_means.predict(df_pca)

        # Appending the scores to the empty lists
        s_scores.append(silhouette_score(df_pca, cluster_assignments))


    # print(s_scores)
    #     #db_scores.append(davies_bouldin_score(df_pca, cluster_assignments))

    # def find_cluster_with_max_s_score(y, x=cluster_cnt):
    #   df = pd.DataFrame(columns=['Cluster Score'], index=[i for i in range(2, len(y)+2)])
    #   df['Cluster Score'] = y
    #   max_cluster = df[df['Cluster Score']==df['Cluster Score'].max()]

    #   return max_cluster['Cluster Score']

    # num_clusters =  find_cluster_with_max_s_score(s_scores)
    # print(num_clusters)
    def plot_evaluation(y, x=cluster_cnt):
        # Creating a DataFrame for returning the max and min scores for each cluster
        s = pd.DataFrame(columns=['Cluster Score'], index=[i for i in range(2, len(y) + 2)])
        s['Cluster Score'] = y
        max_cluster = s[s['Cluster Score'] == s['Cluster Score'].max()]
        #min_cluster = s[s['Cluster Score'] == s['Cluster Score'].min()]
        # print('Max Value:\nCluster #', max_cluster)
        # print('\nMin Value:\nCluster #', min_cluster)

        # Plotting out the scores based on cluster count
        # plt.figure(figsize=(16,6))
        # plt.style.use('ggplot')
        # plt.plot(x,y)
        # plt.xlabel('# of Clusters')
        # plt.ylabel('Score')
        # plt.show()
        print(max_cluster)
        print(max_cluster.index.to_list()[0])
        return (max_cluster.index.to_list()[0])


    # Running the function on the list of scores
    # plot_evaluation(db_scores)  #the lower the better
    num_clusters = plot_evaluation(s_scores)  # s_score the higier the better

    # Instantiating HAC
    hac = AgglomerativeClustering(n_clusters=num_clusters)
    # Fitting
    hac.fit(df_pca)
    # Getting cluster assignments
    cluster_assignments = hac.labels_
    # Unscaling the categories then replacing the scaled values
    # print('))))))))))))df.columns[0:1]:' ,df.columns[0:1])
    # print('%%%%%%%df%%%%%',df)
    df_cluster = df0[['combined_description', 'username']].join(
        pd.DataFrame(scaler.inverse_transform(df.drop('combined_description', axis=1)), columns=df.columns[1:]))
    # Assigning the clusters to each profile
    df_cluster['Cluster #'] = cluster_assignments
    # Viewing the dating profiles with cluster assignments
    df_cluster = df_cluster.set_index('username')
    df_cluster

    import random
    leftover = []
    matched_result = {}
    collection = db.collection('Matches')  # opens  collection
    for i in range(num_clusters):
      print("cluster", i, ": ")
      group = df_cluster[df_cluster['Cluster #']==i].drop('Cluster #', axis=1)
      if group.shape[0] == 1:
        print("group: ",group)
        leftover.append(group)
      else:
        cluster_x = vectorizer.fit_transform(group['combined_description'])
        cluster_v = pd.DataFrame(cluster_x.toarray(), index=group.index, columns=vectorizer.get_feature_names())
        group = group.join(cluster_v)
        group.drop('combined_description', axis=1, inplace=True)
        corr_group = group.T.corr()
        #print(corr_group)
        for user in corr_group.index:
          top_10_sim = corr_group[[user]].sort_values(by=[user],axis=0, ascending=False)[1:]
          print("user: ",user)
          print("all matches: ",top_10_sim)
          print(type(top_10_sim))

          print('-----------')
          #best_match = top_10_sim.index[0]
          #best_matches = top_10_sim.to_dict().values()


          best_matches = top_10_sim[user].to_dict()

          print("**********", best_matches)

          print("\nThe most similar user to User ", user, "is User ", best_matches)
          #matched_result.update({user:best_match})
          matched_result.update({user:best_matches})
          collection.document(user).set(dict({'matches':best_matches}))

    #print('leftover ',leftover)
    leftover_df = pd.concat(leftover)
    #print('leftover ',leftover_df)
    for user in leftover_df.index:
      excluded = [user]
      indices = leftover_df.index.difference(excluded)
      indx = pd.IndexSlice[indices.values]

      random_user = random.choice(indx)
      matched_result.update({user : random_user})
      collection.document(user).set({'matches' : {random_user:0}})
      print( user + ' and '+random_user +' may not be similar, but can have fun together')

    return matched_result
    #print(matched_result)




