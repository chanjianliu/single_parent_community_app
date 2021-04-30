# import SentimentIntensityAnalyzer class
# from vaderSentiment.vaderSentiment module.
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

from datetime import datetime

# function to print sentiments
# of the sentence.
def sentiment_scores(sentence):
    # Create a SentimentIntensityAnalyzer object.
    sid_obj = SentimentIntensityAnalyzer()

    # polarity_scores method of SentimentIntensityAnalyzer
    # oject gives a sentiment dictionary.
    # which contains pos, neg, neu, and compound scores.
    sentiment_dict = sid_obj.polarity_scores(sentence)
    neg = sentiment_dict['neg']
    neu = sentiment_dict['neu']
    pos = sentiment_dict['pos']
    now = datetime.now()

    print("now =", now)
    timestamp = now.strftime("%d/%m/%Y %H:%M:%S")
    return {"pos":pos, "neu":neu, "neg":neg, "timestamp":timestamp}
    # print("Overall sentiment dictionary is : ", sentiment_dict)
    # print("sentence was rated as ", sentiment_dict['neg'] * 100, "% Negative")
    # print("sentence was rated as ", sentiment_dict['neu'] * 100, "% Neutral")
    # print("sentence was rated as ", sentiment_dict['pos'] * 100, "% Positive")
    #
    # print("Sentence Overall Rated As", end=" ")
    #
    # # decide sentiment as positive, negative and neutral
    # if sentiment_dict['compound'] >= 0.05:
    #     print("Positive")
    #
    # elif sentiment_dict['compound'] <= - 0.05:
    #     print("Negative")
    #
    # else:
    #     print("Neutral")

    # Driver code


# if __name__ == "__main__":
#     print("\n1st statement :")
#     sentence = "Hey, I found that your ideas about child education are very thoughtful and interesting!"
#
# # function calling
# sentiment_scores(sentence)
#
# print("\n2nd Statement :")
# sentence = "Let's hangout this Sunday, kids can swim and run around together at the beach"
# sentiment_scores(sentence)
#
# print("\n3rd Statement :")
# sentence = "I don't think it make much sense, I don't like the way they teach kids swimming"
# sentiment_scores(sentence)
