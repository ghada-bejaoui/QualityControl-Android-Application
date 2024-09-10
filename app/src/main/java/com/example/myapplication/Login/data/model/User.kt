package com.example.myapplication.Login.data.model

import java.io.Serializable


data class UserResponse (var id: String? = "", //TOKEN Loopback
                         var ttl: Int? = 0,
                         var created: String? = "",
                         var userId: String? = "",
                         var user: User? = null,): Serializable {}
data class User (
    var id: String? = "",
    var idX3: String? = "",
    var email: String? = "",
    var username: String? = "",
    var userToken: String? = "",
    var permission: String? = "",
    var compteEnabled: Boolean = false,
    var locations: String? = "",
    var permissions: ArrayList<String>? = ArrayList(),
    var role:ArrayList<Role>? = ArrayList(),
    var sites:ArrayList<Site>? = ArrayList(),
    var cities:ArrayList<City>? = ArrayList(),
    var goverments:ArrayList<Site>? = ArrayList(),
    var lastname: String = "",
    var isAdmin: Boolean = false
    ): Serializable {
}

data class Role(var id: String = "",
                var role: String = "",
                var designation:String = ""): Serializable {

}

data class Site(var id: String = "",
                var designation: String = "",
                var gouvernorat: String = "",
): Serializable {
}

data class City(var id: String = "",
                var designation: String = "",
                var goverment: String = "",
                var gouvernorat: String = "",
): Serializable {
}