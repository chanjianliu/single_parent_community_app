import requests
import clean_json
import extractIdAndCommentFromJSON
import json
import sentimentalScore
import firebase_admin
from firebase_admin import credentials, firestore

#
# url = "127.0.0.1:5001/test/1"
# data = {'sender': 'Alice', 'receiver': 'Bob', 'message': 'We did it!'}
# headers = {'Content-type': 'application/json', 'Accept': 'text/plain'}
# res = requests.post(url, data=json.dumps(data), headers=headers)
#
# data = {'sender':   'Alice',
#     'receiver': 'Bob',
#     'message':  'We did it!'}
# res = requests.post("127.0.0.1:5001/test/1", json={'json_payload': data})


# res = requests.post('127.0.0.1:5001/test/1', json={"mytext":"lalala"})
# if res.ok:
#     print(res.json())


# r = requests.get('http://localhost:8080/getUsers')
# data0 = r.json()
# print(data0)

#r1 = requests.get('http://localhost:8080/getTextByUserId/'+ "nbN7MJCoEUcfdU4ZeGrGgZWGlQI3")
     #r1 = requests.get('http://localhost:8080/getUsers')
# print(r1.json())  #list
#print(r1.json()) #string
#
# retrievedComments = requests.get('http://localhost:8080/getcomments').json()
# print(retrievedComments)


cred = credentials.Certificate('./FireStore.json')
firebase_admin.initialize_app(cred,
                              {
                                  'databaseURL': 'https://FireBase.firebaseio.com/'
                              })
db = firestore.client()


from flask import Flask, request, jsonify
app = Flask(__name__)

@app.route('/sacalculation', methods=['GET','POST'])
def add_message():

    userId = request.args.get('x', type=str)
    #r1 = requests.get('http://localhost:8080/getTextByUserId/'+ userId)
    #r1 = requests.get('http://localhost:8080/getUsers')
    retrievedComments = requests.get('http://localhost:8080/getcomments').json()
    # dict = {}
    # for i in range(len(retrievedComments)):
    #     item = retrievedComments[i]
    #     name, comment = extractIdAndCommentFromJSON.extract(item)
    #
    #     if name not in dict.keys():
    #         dict.update({name:comment})
    #     else:
    #         old_comment = dict.get(name)
    #         new_comment = old_comment + " "+ comment
    #         dict.update({name:new_comment})
    id_comments_dict = clean_json.clean(retrievedComments)
    result_dict = {}
    collection = db.collection('SA_Score')
    for id in id_comments_dict.keys():
        scores = sentimentalScore.sentiment_scores(id_comments_dict.get(id))

        #scores = sentimentalScore.sentiment_scores(comment)
        result_dict.update({id: scores})
        #collection.document(id).set({id:scores})
        collection.document(id).set(scores)
    #return id_comments_dict




    return result_dict


    #return jsonify({"type":r1})
    # data0 = r1.json()[0]
    # data1 = r1.json()[1]
    # #result = sentimentalScore.sentiment_scores(data)
    # print(type(data0))
    # data0.update(data1)
    # return jsonify({"answer": data1})
    #print("test ",content)
    #return jsonify({"userId":userId, "comment": data, "postive score":result[0], "neutral score": result[1], "negative score":result[2] })
    #return jsonify({"number": r1})
    #return jsonify({"number":number})



if __name__ == '__main__':
    app.run(port=5001, debug=True)
