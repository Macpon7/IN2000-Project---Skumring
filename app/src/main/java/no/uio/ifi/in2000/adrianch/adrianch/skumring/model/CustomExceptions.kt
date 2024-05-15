package no.uio.ifi.in2000.adrianch.adrianch.skumring.model

class InternetException (override val message: String, override val cause: Throwable): Exception()
class LocationPermissionException (override val message: String): Exception()
class APIException (override val message: String): Exception()
