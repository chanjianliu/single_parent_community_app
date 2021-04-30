import matching_complete_logic
import requests
from flask import Flask, request, jsonify
import pandas as pd
app = Flask(__name__)

# retrievedUsers = requests.get('http://localhost:8080/getusers').json()
# print(retrievedUsers)

def extract(json_obj):
    dict = {}

    username = json_obj.get("username")
    selfDescription = json_obj.get("selfDescription")
    profession = json_obj.get("profession")
    interest = json_obj.get("interest")
    numberOfKids = json_obj.get("numberOfKids")
    if username is not None :
        dict.update({'username':username})
    if selfDescription is not None:
        dict.update({'selfDescription':selfDescription})
    if profession is not None:
        dict.update({'profession':profession})
    if interest is not None:
        dict.update({'interest':interest})
    if numberOfKids is not None:
        dict.update({'numberOfKids':numberOfKids})
    return dict

@app.route('/matchingcalculation', methods=['GET','POST'])
def add_message():


    retrievedUsers = requests.get('http://localhost:8080/getusers').json()

    list = []
    for i in range(len(retrievedUsers)):
        item = retrievedUsers[i]
        record = extract(item)
        list.append(record)
    df = pd.DataFrame(list)
    df.to_csv("test.csv",index=False)
    #print(df)
    return matching_complete_logic.matching_algo()



if __name__ == '__main__':
    app.run(port=5002, debug=True)
