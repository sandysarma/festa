package com.example.festa.view.events.viewmodel.createNewAlbum

import com.google.gson.annotations.SerializedName

data class CreateAlbumBody(@SerializedName("albumName") var albumName: String)