package com.janrain.taskomatic.data

import org.scalatra._
import scala.slick.driver.PostgresDriver.simple._
import slick.driver.PostgresDriver
// import com.github.tminglei.slickpg._
import scala.slick.session.Database
import org.postgresql.ds.PGSimpleDataSource
import Database.threadLocalSession
// import com.mchange.v2.c3p0.ComboPooledDataSource

trait PostgresSupport {
	val session = Database.forURL(url = "jdbc:postgresql://localhost:5432/taskomatic",
		                     driver = "org.postgresql.Driver")
}

object PostgresSupport extends PostgresSupport