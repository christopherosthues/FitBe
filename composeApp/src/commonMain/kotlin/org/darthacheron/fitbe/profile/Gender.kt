package org.darthacheron.fitbe.profile

import fitbe.composeapp.generated.resources.Res
import fitbe.composeapp.generated.resources.profile_gender_female
import fitbe.composeapp.generated.resources.profile_gender_male
import fitbe.composeapp.generated.resources.profile_gender_other
import fitbe.composeapp.generated.resources.profile_gender_unknown
import org.jetbrains.compose.resources.StringResource

enum class Gender {
    UNKNOWN, MALE, FEMALE, OTHER;

    fun toStringResource(): StringResource {
        return when (this) {
            UNKNOWN -> Res.string.profile_gender_unknown
            MALE -> Res.string.profile_gender_male
            FEMALE -> Res.string.profile_gender_female
            OTHER -> Res.string.profile_gender_other
        }
    }
}