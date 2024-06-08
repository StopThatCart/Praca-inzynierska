import csv
import pandas as pd
import re

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
        
        
check_labels(csv_filename, "grupa_roslin")