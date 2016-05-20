"""
This map editor is used to help create locations.txt.
"""

from location_parser import LocationParser
from graphical_editor import GraphicalEditor

LOCATIONS_TXT_FILE = 'locations.txt'
MAP_FILE = 'map_2400.png'

def main():
  map_data = LocationParser.parse(LOCATIONS_TXT_FILE)
  GraphicalEditor(map_data, MAP_FILE)

if __name__ == '__main__':
  main()
