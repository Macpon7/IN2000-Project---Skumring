package no.uio.ifi.in2000.adrianch.adrianch.skumring.data


/*
har en fetch all paths  som er en liste av strings, dvs presetpins

Dette er inne i en viewmodel
        fun fetchAllPaths(triplet List<List<String>>){
            CourutineScope(Dispatcher.IO).launch{
                val responses = path.map{ triplet ->
                    async{
                        SunriseDataSource.fetchSunActivity(triplet):(path)
                    }
                }.awaitAll() //venter på at alt er ferdig
            }
        }
 */