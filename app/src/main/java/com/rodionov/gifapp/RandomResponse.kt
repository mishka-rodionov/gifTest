package com.rodionov.gifapp
import com.google.gson.annotations.SerializedName

/**
 * Created by mJafarinejad on 8/11/2018.
 */
class RandomResponse(

//        metaModel: MetaModel,

        @SerializedName("data")
        val gifObjectModel: GifObjectModel

) /*: BaseResponse(metaModel)*/

