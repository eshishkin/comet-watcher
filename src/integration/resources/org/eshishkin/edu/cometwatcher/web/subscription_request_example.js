{
    "email": #{if}($user) "${user}"#{else} "user@example.com"#{end},
    "observerLatitude": #{if} ($latitude) "${latitude}"#{else} "0"#{end},
    "observerLongitude": #{if} ($longitude) "${longitude}"#{else} "0"#{end},
    "observerAltitude": #{if} ($altitude) ${altitude}#{else} 0#{end},
    "observerTimeZone": "UTC",
    "interval": #{if}($interval) "${interval}"#{else} "DAILY"#{end},
    "desiredStarMagnitude": #{if}($magnitude) ${magnitude}#{else} 10#{end}

}