package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding

data class ForwardGeocoding(
    val attribution: String,
    val features: List<ForwardFeature>,
    val type: String
)

data class ForwardContext(
    val address: Address,
    val country: Country,
    val district: District,
    val locality: Locality,
    val place: Place,
    val postcode: Postcode,
    val region: Region,
    val street: Street
)

data class District(
    val mapbox_id: String,
    val name: String,
    val wikidata_id: String
)

data class ForwardFeature(
    val geometry: Geometry,
    val id: String,
    val properties: ForwardProperties,
    val type: String
)

data class Locality(
    val mapbox_id: String,
    val name: String,
    val wikidata_id: String
)

data class MatchCode(
    val address_number: String,
    val confidence: String,
    val country: String,
    val locality: String,
    val place: String,
    val postcode: String,
    val region: String,
    val street: String
)

data class ForwardProperties(
    val context: ForwardContext,
    val coordinates: Coordinates,
    val feature_type: String,
    val full_address: String,
    val mapbox_id: String,
    val match_code: MatchCode,
    val name: String,
    val name_preferred: String,
    val place_formatted: String
)