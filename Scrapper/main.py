import csv
import pandas as pd
import re
from neo4j import GraphDatabase
from process_heights import main as process_height

# Parametry do połączenia z bazą danych Neo4j
uri = "bolt://localhost:7687"
username = "neo4j"
password = "11111111"

csv_filename = "katalog_roslin.csv"

def get_labels(file_path, label):
    estimated_heights = []
    with open(file_path, mode='r', encoding='utf-8') as file:
        csv_reader = csv.DictReader(file)
        for row in csv_reader:
            estimated_height = row[label]
            estimated_heights.append(estimated_height)
    return estimated_heights

def check_labels(csv_filename, label):
    groups = get_labels(csv_filename, label)
    group_set = set()
    for group in groups:
        group_set.add(group)
    for group in group_set:
        print(group)
        
        
#check_labels(csv_filename, "grupa_roslin")

test = "Brak"
heights = process_height(test)
for h in heights:
    if h is not None:
        print("bep")
    print(h)

#for height in heights:
#    if height['name'] != "Brak":
#        height_params = {k: v for k, v in height.items() if v is not None}
    
#    if height_params:
#        height_props = ", ".join([f"{key}: '{value}'" for key, value in height_params.items()])
#        print(height_props)

for h in heights:
    #First try to create a query with properties.
    query = "MATCH (u:Wysokosc {{ {properties} }}) RETURN u"
    #Define your paramters: warning remember that in python, dictionnary keys must be string (well, not only, but in this case yes)
    #parameters = {"id": 1, "name": "Toto"}
    parameters = h
    #Create the properties list for py2neo
    properties = ', '.join('{0}: ${0}'.format(n) for n in parameters)
    #Format your query:
    query2 = query.format(properties=properties)
    print(query2)

    #query2 now look like this:
    #MATCH (u:User { id: $id, name: $name }) RETURN u.name

    #Finally, you can run query2 with parameters:
    driver = GraphDatabase.driver(uri, auth=(username, password))
    with driver.session() as session:
        pass
       # result = session.run(query2, parameters=h)
       # records = result.data()  # This fetches all records as a list of dictionaries
       # print(records)  # Do something with the records here
   # print(r.data())
driver.close()

