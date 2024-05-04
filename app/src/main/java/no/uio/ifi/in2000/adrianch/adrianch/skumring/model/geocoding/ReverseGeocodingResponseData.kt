package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding

data class ReverseGeocoding(
    val attribution: String,
    val features: List<ReverseFeature>,
    val type: String
)

data class Address(
    val address_number: String,
    val mapbox_id: String,
    val name: String,
    val street_name: String
)

data class Coordinates(
    val accuracy: String,
    val latitude: Double,
    val longitude: Double,
    val routable_points: List<RoutablePoint>
)

data class Country(
    val country_code: String,
    val country_code_alpha_3: String,
    val mapbox_id: String,
    val name: String,
    val wikidata_id: String
)

data class ReverseContext(
    val address: Address,
    val country: Country,
    val place: Place,
    val postcode: Postcode,
    val region: Region,
    val street: Street
)

data class ReverseFeature(
    val geometry: Geometry,
    val id: String,
    val properties: ReverseProperties,
    val type: String
)

data class Geometry(
    val coordinates: List<Double>,
    val type: String
)

data class RoutablePoint(
    val latitude: Double,
    val longitude: Double,
    val name: String
)

data class Place(
    val mapbox_id: String,
    val name: String,
    val short_code: String,
    val wikidata_id: String
)

data class Postcode(
    val mapbox_id: String,
    val name: String
)

data class ReverseProperties(
    val additional_feature_types: List<String>,
    val bbox: List<Double>,
    val context: ReverseContext,
    val coordinates: Coordinates,
    val feature_type: String,
    val full_address: String,
    val mapbox_id: String,
    val name: String,
    val name_preferred: String,
    val place_formatted: String
)

data class Region(
    val mapbox_id: String,
    val name: String,
    val region_code: String,
    val region_code_full: String,
    val wikidata_id: String
)

data class Street(
    val mapbox_id: String,
    val name: String
)