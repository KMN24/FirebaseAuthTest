package kg.kmn.test

class User() {
    var id : String = ""
    var name : String = ""
    var secName : String = ""
    var email : String = ""
    var imageId : String = ""

    constructor(id: String, name: String, secName: String, email: String, imageId: String) : this(){
        this.id = id
        this.name = name
        this.secName = secName
        this.email = email
        this.imageId = imageId
    }

}