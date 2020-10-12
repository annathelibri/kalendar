package net.notjustanna.calendar

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.github.nanoflakes.NanoflakeLocalGenerator
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import net.notjustanna.calendar.dao.JdbiDataAccessObject
import net.notjustanna.calendar.handlers.*

fun main() {
    val app = Javalin.create {
        it.addStaticFiles("/static")
        it.showJavalinBanner = false
    }

    val dao = JdbiDataAccessObject("jdbc:postgresql://localhost:15432/kalendar?user=postgres&password=tokenlabkalendar")
    val generator = NanoflakeLocalGenerator(KALENDAR_EPOCH, 0)
    val algorithm = Algorithm.HMAC256(System.getenv("kalendar_secret") ?: "kalendar_is_very_secret_indeed")
    val verifier = JWT.require(algorithm).withIssuer("kalendar").build()

    app.routes {
        path("api") {
            post("login", LoginHandler(dao, algorithm))
            post("register", RegisterHandler(dao, algorithm, generator))
            get("events", EventsHandler(dao, verifier))
            //get("events", MockEventsHandler(generator))
            get("event/:id", GetEventHandler(dao, verifier))
            post("event", CreateEventHandler(dao, generator, verifier))
            patch("event/:id", PatchEventHandler(dao, verifier))
            delete("event/:id", DeleteEventHandler(dao, verifier))
        }
    }

    app.start(System.getenv("kalendar_port")?.toIntOrNull() ?: 8080)
}