def extract(json_obj):
    name = json_obj.get("userId")
    body = json_obj.get("body")
    return name, body