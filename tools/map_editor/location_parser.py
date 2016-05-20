from map_data import MapData

class LocationParser:
  """
  Location Parser. Only attempt to parse a subset of the commands needed
  for the map editor, ignore everything else.
  """
  @staticmethod
  def parse(location_txt_file):
    text_file = open(location_txt_file, "r")
    locations = []
    passive_locations = []
    paths = []
    name_to_coord = {}

    for line in text_file.readlines():
      line = line.strip()
      if len(line) == 0 or line[0] == '#':
        continue
      tokens = line.split(" ")
      command = tokens[0]

      if command == "location":
        locations.append((tokens[1],int(tokens[2]),int(tokens[3])))
        name_to_coord[tokens[1]] = (int(tokens[2]),int(tokens[3]))

      elif command == "passive_location":
        passive_locations.append((tokens[1],int(tokens[2]),int(tokens[3])))
        name_to_coord[tokens[1]] = (int(tokens[2]),int(tokens[3]))

      elif command == "path":
        path1 = tokens[1]
        path2 = tokens[2]
        paths.append((name_to_coord[path1],name_to_coord[path2]))

    return MapData(
      locations=locations,
      passive_locations=passive_locations,
      paths=paths,
      name_to_coord=name_to_coord)
