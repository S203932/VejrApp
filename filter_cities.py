"""Create a cities dataset."""

import json
from dataclasses import dataclass


@dataclass
class City:
    """Class for deserializing and simplifying dataset."""

    country: str
    latitude: float
    longitude: float
    name: str
    population: int
    timezone: str


def get_population(city: City):
    """Key for sorting."""
    return city.population


def main() -> None:
    """Filter main dataset."""
    base_path = "/Users/markusjacobsen/Downloads/"
    filter_size = 100000

    # Dataset from
    # https://public.opendatasoft.com/explore/dataset/geonames-all-cities-with-a-population-500/api/?disjunctive.country
    input_filename = f"{base_path}geonames-all-cities-with-a-population-500.json"
    output_filename = f"{base_path}filtered_dataset_{filter_size}_tz.json"

    filtered_cities = []

    with open(input_filename, encoding="utf-8") as allCities:
        all_cities_string = json.load(allCities)

        for city in all_cities_string:
            if city["population"] > filter_size:
                filtered_cities.append(
                    City(
                        country=city["country"],
                        latitude=city["latitude"],
                        longitude=city["longitude"],
                        name=city["ascii_name"],
                        population=city["population"],
                        timezone=city["timezone"],
                    )
                )

    filtered_cities.sort(key=get_population, reverse=True)

    with open(output_filename, encoding="utf-8", mode="w") as all_cities_filtered:
        json.dump(
            [filtered_city.__dict__ for filtered_city in filtered_cities],
            all_cities_filtered,
        )


if __name__ == "__main__":
    main()