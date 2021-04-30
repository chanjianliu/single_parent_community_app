import  extractIdAndCommentFromJSON

def clean(retrievedComments):
    id_comments = {}
    for i in range(len(retrievedComments)):
        item = retrievedComments[i]
        name, comment = extractIdAndCommentFromJSON.extract(item)

        if name not in id_comments.keys():
            id_comments.update({name:comment})
            #print(name," ", comment)
        else:
            #print(name, comment)
            old_comment = id_comments.get(name)
            new_comment = old_comment + " "+ comment
            #print(name," ",new_comment)

            id_comments.update({name:new_comment})
    return id_comments