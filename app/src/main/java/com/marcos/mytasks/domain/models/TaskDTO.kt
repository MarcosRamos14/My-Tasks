package com.marcos.mytasks.domain.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.marcos.mytasks.framework.firebase.FirebaseHelper
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TaskDTO(
    var id: String = "",
    var description: String = "",
    var status: Int = 0
) : Parcelable {
    init {
        this.id = FirebaseHelper.getDatabase().push().key ?: ""
    }
}