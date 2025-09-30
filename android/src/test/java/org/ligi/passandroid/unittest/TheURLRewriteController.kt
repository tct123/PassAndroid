package org.ligi.passandroid.unittest

import androidx.core.net.toUri
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ligi.passandroid.Tracker
import org.ligi.passandroid.ui.quirk_fix.URLRewriteController
import org.mockito.Mockito.mock

class TheURLRewriteController {

    private val tested = URLRewriteController(mock(Tracker::class.java))

    @org.junit.jupiter.api.Test
    fun testAppSpotRewrite() {
        val res = tested.getUrlByUri("http://pass-cloud.appspot.com/open_or_install?url=http://espass.it/assets/download/pass/movie.espass".toUri())

        assertThat(res).isEqualTo("http://espass.it/assets/download/pass/movie.espass")
    }

    @org.junit.jupiter.api.Test
    fun testPass2URewrite() {
        val res = tested.getUrlByUri("pass2u://import/https://api.passdock.com/passes/17969/e5dfb0afff61b1294235918a6a9ac75255daa89f.pkpass".toUri())

        assertThat(res).isEqualTo("https://api.passdock.com/passes/17969/e5dfb0afff61b1294235918a6a9ac75255daa89f.pkpass")
    }


    @org.junit.jupiter.api.Test
    fun testRejection() {
        val res = tested.getUrlByUri("http://foo.bar".toUri())

        assertThat(res).isNull()
    }

    @org.junit.jupiter.api.Test
    fun testThatBrusselWorks() {
        val res = tested.getUrlByUri("http://prod.wap.ncrwebhost.mobi/mobiqa/wap/14foo/83bar/".toUri())

        assertThat(res).isEqualTo("http://prod.wap.ncrwebhost.mobi/mobiqa/wap/14foo/83bar/passbook")
    }


    @Test
    fun testThatSwissWorks() {
        val res = tested.getUrlByUri("http://mbp.swiss.com/mobiqa/wap/14foo/83bar/".toUri())

        assertThat(res).isEqualTo("http://prod.wap.ncrwebhost.mobi/mobiqa/wap/14foo/83bar/passbook")
    }

    @org.junit.jupiter.api.Test
    fun testThatCathayWorks() {
        val res = tested.getUrlByUri("https://www.cathaypacific.com/foo?v=bar".toUri())

        assertThat(res).isEqualTo("https://www.cathaypacific.com/icheckin2/PassbookServlet?v=bar")
    }


    @org.junit.jupiter.api.Test
    fun testVirgin1() {
        val res = tested.getUrlByUri("https://bazz.virginaustralia.com/boarding/CheckInApiIntegration?key=foo".toUri())

        assertThat(res).isEqualTo("https://mobile.virginaustralia.com/boarding/pass.pkpass?key=foo")
    }


    @org.junit.jupiter.api.Test
    fun testVirgin() {
        val res = tested.getUrlByUri("https://bazz.virginaustralia.com/boarding/pass.pkpass?c=foo".toUri())

        assertThat(res).isEqualTo("https://mobile.virginaustralia.com/boarding/pass.pkpass?key=foo")
    }

    @org.junit.jupiter.api.Test
    fun testAirCanada() {
        val res = tested.getUrlByUri("http://m.aircanada.ca/ebp/XYZ".toUri())

        assertThat(res).isEqualTo("http://m.aircanada.ca/ebp/XYZ?appDetection=false")
    }
}
