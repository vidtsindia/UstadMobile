/*
    This file is part of Ustad Mobile.

    Ustad Mobile Copyright (C) 2011-2014 UstadMobile Inc.

    Ustad Mobile is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version with the following additional terms:

    All names, links, and logos of Ustad Mobile and Toughra Technologies FZ
    LLC must be kept as they are in the original distribution.  If any new
    screens are added you must include the Ustad Mobile logo as it has been
    used in the original distribution.  You may not create any new
    functionality whose purpose is to diminish or remove the Ustad Mobile
    Logo.  You must leave the Ustad Mobile logo as the logo for the
    application to be used with any launcher (e.g. the mobile app launcher).

    If you want a commercial license to remove the above restriction you must
    contact us.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

    Ustad Mobile is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

 */
package com.ustadmobile.core.impl;

/**
 *
 * @author mike
 */
public class UstadMobileConstants {
    
    /**
     * Hardcode UTF8 Encoding String - value "UTF-8"
     */
    public static final String UTF8 = "UTF-8";
    
    /**
     * Hardcoded subdirectory used for caching
     */
    public static final String CACHEDIR = "cache";
    
    /**
     * Sorted by country name: list of international dialing codes
     * see testres/countrycodes.ods
     */
    public static final short[] COUNTRYDIALINGCODES = new short[]{
        93,
        355,
        213,
        376,
        244,
        54,
        374,
        297,
        61,
        43,
        994,
        973,
        880,
        375,
        32,
        501,
        229,
        975,
        591,
        387,
        267,
        55,
        673,
        359,
        226,
        257,
        855,
        237,
        1,
        238,
        236,
        235,
        56,
        86,
        57,
        269,
        242,
        682,
        506,
        385,
        53,
        357,
        420,
        243,
        45,
        246,
        253,
        593,
        20,
        503,
        240,
        291,
        372,
        251,
        388,
        500,
        298,
        679,
        358,
        33,
        262,
        689,
        241,
        220,
        995,
        49,
        233,
        350,
        30,
        299,
        590,
        502,
        594,
        224,
        245,
        592,
        509,
        504,
        852,
        36,
        354,
        91,
        62,
        98,
        964,
        353,
        972,
        39,
        225,
        81,
        962,
        7,
        254,
        686,
        850,
        82,
        965,
        996,
        856,
        371,
        961,
        266,
        231,
        218,
        423,
        370,
        352,
        853,
        389,
        261,
        265,
        60,
        960,
        223,
        356,
        692,
        596,
        222,
        230,
        52,
        691,
        373,
        377,
        976,
        382,
        212,
        258,
        95,
        264,
        674,
        977,
        31,
        599,
        687,
        64,
        505,
        227,
        234,
        683,
        47,
        968,
        92,
        680,
        970,
        507,
        675,
        595,
        51,
        63,
        48,
        351,
        974,
        40,
        7,
        250,
        290,
        378,
        239,
        966,
        221,
        381,
        248,
        232,
        65,
        421,
        386,
        677,
        252,
        27,
        34,
        94,
        508,
        249,
        597,
        268,
        46,
        41,
        963,
        886,
        992,
        255,
        66,
        670,
        228,
        690,
        676,
        216,
        90,
        993,
        688,
        256,
        380,
        971,
        44,
        1,
        598,
        998,
        678,
        58,
        84,
        681,
        685,
        967,
        260,
        263
    };
    
    public static final String[] COUNTRYCODES = new String[] {
        "AF",
        "AL",
        "DZ",
        "AD",
        "AO",
        "AR",
        "AM",
        "AW",
        "AU",
        "AT",
        "AZ",
        "BH",
        "BD",
        "BY",
        "BE",
        "BZ",
        "BJ",
        "BT",
        "BO",
        "BA",
        "BW",
        "BR",
        "BN",
        "BG",
        "BF",
        "BI",
        "KH",
        "CM",
        "CA",
        "CV",
        "CF",
        "TD",
        "CL",
        "CN",
        "CO",
        "KM",
        "CG",
        "CK",
        "CR",
        "HR",
        "CU",
        "CY",
        "CZ",
        "CD",
        "DK",
        "IO",
        "DJ",
        "EC",
        "EG",
        "SV",
        "GQ",
        "ER",
        "EE",
        "ET",
        "EU",
        "FK",
        "FO",
        "FJ",
        "FI",
        "FR",
        "",
        "PF",
        "GA",
        "GM",
        "GE",
        "DE",
        "GH",
        "GI",
        "GR",
        "GL",
        "GP",
        "GT",
        "GF",
        "GN",
        "GW",
        "GY",
        "HT",
        "HN",
        "",
        "HU",
        "IS",
        "IN",
        "ID",
        "IR",
        "IQ",
        "IE",
        "IL",
        "IT",
        "CI",
        "JP",
        "JO",
        "KZ",
        "KE",
        "KI",
        "KP",
        "KR",
        "KW",
        "KY",
        "LA",
        "LV",
        "LB",
        "LS",
        "LR",
        "LY",
        "LI",
        "LT",
        "LU",
        "",
        "MK",
        "MG",
        "MW",
        "MY",
        "MV",
        "ML",
        "MT",
        "MH",
        "MQ",
        "MR",
        "MU",
        "MX",
        "FM",
        "MD",
        "MC",
        "MN",
        "ME",
        "MA",
        "MZ",
        "MM",
        "NA",
        "NR",
        "NP",
        "NL",
        "AN",
        "NC",
        "NZ",
        "NI",
        "NE",
        "NG",
        "NU",
        "NO",
        "OM",
        "PK",
        "PW",
        "PS",
        "PA",
        "PG",
        "PY",
        "PE",
        "PH",
        "PL",
        "PT",
        "QA",
        "RO",
        "RU",
        "RW",
        "SH",
        "SM",
        "ST",
        "SA",
        "SN",
        "RS",
        "SC",
        "SL",
        "SG",
        "SK",
        "SI",
        "SB",
        "SO",
        "ZA",
        "ES",
        "LK",
        "PM",
        "SD",
        "SR",
        "SQ",
        "SE",
        "CH",
        "SY",
        "TW",
        "TJ",
        "TZ",
        "TH",
        "TL",
        "TG",
        "TK",
        "TO",
        "TN",
        "TR",
        "TM",
        "TV",
        "UG",
        "UA",
        "AE",
        "UK",
        "US",
        "UY",
        "UZ",
        "VU",
        "VE",
        "VN",
        "WF",
        "WS",
        "YE",
        "ZM",
        "ZW"
    };
    
    
    /**
     * Sorted by country name: list of countries
     */
    public static final String[] COUNTRYNAMES = new String[]{
        "Afghanistan",
        "Albania",
        "Algeria",
        "Andorra",
        "Angola",
        "Argentina",
        "Armenia",
        "Aruba",
        "Australia",
        "Austria",
        "Azerbaijan",
        "Bahrain",
        "Bangladesh",
        "Belarus",
        "Belgium",
        "Belize",
        "Benin",
        "Bhutan",
        "Bolivia",
        "Bosnia - Herzegovina",
        "Botswana",
        "Brazil",
        "Brunei Darussalam",
        "Bulgaria",
        "Burkina Faso",
        "Burundi",
        "Cambodia",
        "Cameroon",
        "Canada",
        "Cape Verde",
        "Central African Rep.",
        "Chad",
        "Chile",
        "China",
        "Colombia",
        "Comoros (only)",
        "Congo (Brazzaville)",
        "Cook Islands",
        "Costa Rica",
        "Croatia",
        "Cuba",
        "Cyprus",
        "Czech Republic",
        "Dem. Rep. Congo",
        "Denmark",
        "Diego Garcia",
        "Djibouti",
        "Ecuador",
        "Egypt",
        "El Salvador",
        "Equatorial Guinea",
        "Eritrea",
        "Estonia",
        "Ethiopia",
        "European Numbers",
        "Falkland Islands",
        "Faroe Islands",
        "Fiji",
        "Finland",
        "France",
        "French Indian Ocean",
        "French Polynesia",
        "Gabon",
        "Gambia",
        "Georgia",
        "Germany",
        "Ghana",
        "Gibraltar",
        "Greece",
        "Greenland",
        "Guadeloupe",
        "Guatemala",
        "Guiana (French)",
        "Guinea",
        "Guinea-Bissau",
        "Guyana",
        "Haiti",
        "Honduras",
        "Hong Kong",
        "Hungary",
        "Iceland",
        "India",
        "Indonesia",
        "Iran",
        "Iraq",
        "Ireland",
        "Israel",
        "Italy",
        "Ivory Coast",
        "Japan",
        "Jordan",
        "Kazakhstan",
        "Kenya",
        "Kiribati",
        "Korea (North)",
        "Korea (South)",
        "Kuwait",
        "Kyrgyzstan",
        "Laos",
        "Latvia",
        "Lebanon",
        "Lesotho",
        "Liberia",
        "Libya",
        "Liechtenstein",
        "Lithuania",
        "Luxembourg",
        "Macau",
        "Macedonia",
        "Madagascar",
        "Malawi",
        "Malaysia",
        "Maldives",
        "Mali",
        "Malta",
        "Marshall Islands",
        "Martinique",
        "Mauritania",
        "Mauritius",
        "Mexico",
        "Micronesia",
        "Moldova",
        "Monaco",
        "Mongolia",
        "Montenegro",
        "Morocco",
        "Mozambique",
        "Myanmar",
        "Namibia",
        "Nauru",
        "Nepal",
        "Netherlands",
        "Netherlands Antilles",
        "New Caledonia",
        "New Zealand",
        "Nicaragua",
        "Niger",
        "Nigeria",
        "Niue",
        "Norway",
        "Oman",
        "Pakistan",
        "Palau",
        "Palestine",
        "Panama",
        "Papua New Guinea",
        "Paraguay",
        "Peru",
        "Philippines",
        "Poland",
        "Portugal",
        "Qatar",
        "Romania",
        "Russia",
        "Rwanda",
        "Saint Helena",
        "San Marino",
        "São Tomé & Príncipe",
        "Saudi Arabia",
        "Senegal",
        "Serbia",
        "Seychelles",
        "Sierra Leone",
        "Singapore",
        "Slovakia",
        "Slovenia",
        "Solomon Islands",
        "Somalia",
        "South Africa",
        "Spain",
        "Sri Lanka",
        "St Pierre & Miquélon",
        "Sudan",
        "Suriname",
        "Swaziland",
        "Sweden",
        "Switzerland",
        "Syria",
        "Taiwan",
        "Tajikistan",
        "Tanzania",
        "Thailand",
        "Timor-Leste",
        "Togo",
        "Tokelau",
        "Tonga",
        "Tunisia",
        "Turkey",
        "Turkmenistan",
        "Tuvalu",
        "Uganda",
        "Ukraine",
        "United Arab Emirates",
        "United Kingdom",
        "United States",
        "Uruguay",
        "Uzbekistan",
        "Vanuatu",
        "Venezuela",
        "Viet Nam",
        "Wallis and Futuna",
        "Western Samoa",
        "Yemen",
        "Zambia",
        "Zimbabwe"
    };
}
