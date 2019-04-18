package com.alvinalexander.accesslogparser

case class Request (requestType:String,
                    uri:String,
                    httpVersion:String)