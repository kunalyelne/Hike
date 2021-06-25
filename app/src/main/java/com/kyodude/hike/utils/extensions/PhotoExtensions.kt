package com.kyodude.hike.utils.extensions

import com.kyodude.hike.model.dataModel.Photo

fun Photo.getUri(): String {
    return "http://farm${this.farm}.static.flickr.com/${this.server}/${this.id}_${this.secret}.jpg"
}