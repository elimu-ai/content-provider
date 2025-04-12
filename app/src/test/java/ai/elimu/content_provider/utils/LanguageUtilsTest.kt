package ai.elimu.content_provider.utils

import ai.elimu.model.v2.enums.Language
import org.junit.Assert.*

import org.junit.Test

 class LanguageUtilsTest {

  @Test
  fun `should return Language ENG for 'eng'`() {
   assertEquals(Language.ENG, "eng".toLanguage())
  }

  @Test
  fun `should return Language HIN for 'HIN' (case-insensitive)`() {
   assertEquals(Language.HIN, "HIN".toLanguage())
  }

  @Test
  fun `should return Language TGL for 'tgl'`() {
   assertEquals(Language.TGL, "tgl".toLanguage())
  }

  @Test
  fun `should return Language THA for 'Tha' (case-insensitive)`() {
   assertEquals(Language.THA, "Tha".toLanguage())
  }

  @Test
  fun `should return null for unknown language code`() {
   assertNull("xyz".toLanguage())
  }

  @Test
  fun `should return null for null input`() {
   val input: String? = null
   assertNull(input.toLanguage())
  }
}