package com.amandaluz.movieapplication.data.database.ratingdomain

data class CustomerDomain(
    var id: String,
    var option: OptionDomain,
    var sort: Int
)

data class OptionDomain(
    var buttons: List<CustomerButtonDomain> ,
    var footer: String ,
    var headerTitle: String ,
    var inputs: InputsDomain ,
    var name: String ,
    var pageTemplate: String ,
    var text: String ,
    var urlImage: String
)

data class InputsDomain(
    var dropdownBox: List<DropdownBoxDomain>,
    var dropdownBox1: DropdownBox1Domain,
    var dropdownBox2: DropdownBox2Domain,
    var textBox: CustomerTextBoxDomain
)

data class CustomerButtonDomain(
    var name: String,
    var openWith: String,
    var sendRequest: Boolean,
    val template: String,
    var url: String
)

data class HeaderButtonDomain(
    var name: String,
    var openWith: String,
    var url: String
)

data class DropdownBoxDomain(
    var id: Int,
    var label: String,
    var reason: List<ReasonDomain>
)

data class DropdownBox1Domain(
    var label: String,
    var placeholder: String
)

data class DropdownBox2Domain(
    var label: String,
    var placeholder: String
)

data class ReasonDomain(
    var label: String,
    var openWith: String,
    var url: String?
)

data class CustomerTextBoxDomain(
    var label: String,
    var maxLength: Int,
    var minLength: Int,
    var placeholder: String
)
