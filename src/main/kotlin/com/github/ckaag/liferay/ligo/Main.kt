package com.github.ckaag.liferay.ligo

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.long
import net.sf.expectit.ExpectBuilder
import net.sf.expectit.matcher.Matchers.contains
import org.apache.commons.net.telnet.TelnetClient
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) = Ligo().main(args)

class Ligo : CliktCommand() {

    private val cmd by option(help = "Gogo Shell command to execute").default("lb")
    private val serverHost by option(help = "hostname of telnet server").default("localhost")
    private val serverPort by option(help = "port of telnet server").int().default(11311)
    private val timeoutSeconds by option(help = "timeout for commands (in seconds)").long().default(30L)
    private val linebreak by option(help = "linebreak format (keep default on UNIX)").default("\r\n")
    private val prompt by option(help = "prompt to expect").default("g!")

    override fun run() {
        val inputBuffer = StringBuilder()
        val telnet = TelnetClient()
        LocalDateTime.now()
        telnet.connect(serverHost, serverPort)
        val expect = ExpectBuilder()
            .withOutput(telnet.outputStream)
            .withInputs(telnet.inputStream)
            .withEchoInput(inputBuffer)
            .withExceptionOnFailure()
            .withTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .build()
        expect.use { shell ->
            shell.expect(contains(prompt))
            inputBuffer.clear()
            shell.sendLine(cmd + linebreak)
            shell.expect(contains(prompt))
            println(inputBuffer.toString().let {
                it.substring(
                    cmd.length + (linebreak.length * 2),
                    it.length - cmd.length - (prompt.length * 2)
                )
            })
        }
    }
}