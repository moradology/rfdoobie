initialCommands in console :=
"""
import doobie._, doobie.implicits._
import cats._, cats.data._, cats.effect.IO, cats.implicits._
import doobie.hikari._, doobie.hikari.implicits._

val q = sql"select 42".query[Int].unique

val trans = HikariTransactor.newHikariTransactor[IO]("org.postgresql.Driver", "jdbc:postgresql://database.service.rasterfoundry.internal/", "rasterfoundry", "rasterfoundry")
val p: IO[Int] = for {
  xa <- trans
  _  <- xa.configure(hx => IO( /* do something with hx */ ()))
  a  <- q.transact(xa) guarantee xa.shutdown
} yield a
"""
