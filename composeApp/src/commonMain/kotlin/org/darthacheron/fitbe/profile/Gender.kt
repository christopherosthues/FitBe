package org.darthacheron.fitbe.profile

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.gender_female
import fitbe.composeapp.generated.resources.gender_male
import fitbe.composeapp.generated.resources.gender_other
import fitbe.composeapp.generated.resources.gender_unknown
import org.jetbrains.compose.resources.StringResource

enum class Gender {
    UNKNOWN,
    MALE,
    FEMALE,
    OTHER;

    fun toStringResource(): StringResource =
        when (this) {
            UNKNOWN -> Res.string.gender_unknown
            MALE -> Res.string.gender_male
            FEMALE -> Res.string.gender_female
            OTHER -> Res.string.gender_other
        }
}