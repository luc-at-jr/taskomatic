package com.janrain.taskomatic.data

import org.scalatra._
import org.scalatra.json._
import org.json4s.{DefaultFormats, Formats}

trait JsonSupport extends JacksonJsonSupport {
	protected implicit val jsonFormats: Formats = DefaultFormats

	def jsonMethod = {
		contentType = formats("json")
	}
}