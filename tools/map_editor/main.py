"""
This map editor is used to help create locations.txt.
"""

from location_parser import LocationParser
from graphical_editor import GraphicalEditor

LOCATIONS_TXT_FILE = 'locations.txt'

def main():
  map_data = LocationParser.parse(LOCATIONS_TXT_FILE)
  GraphicalEditor(map_data)

if __name__ == '__main__':
  main()
