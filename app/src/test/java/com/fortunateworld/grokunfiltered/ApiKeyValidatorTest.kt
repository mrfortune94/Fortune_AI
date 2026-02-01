package com.fortunateworld.grokunfiltered

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for API key validation logic in MainActivity.
 */
class ApiKeyValidatorTest {

    @Test
    fun testValidApiKey_withSkPrefix() {
        // Valid: "sk-" prefix with sufficient length (>= 20)
        val key = "sk-abcdefghijklmnopqrstuvwxyz"
        assertTrue("Valid sk- key should be accepted", MainActivity.isValidApiKey(key))
    }

    @Test
    fun testValidApiKey_withXaiPrefix() {
        // Valid: "xai-" prefix with sufficient length (>= 20)
        val key = "xai-abcdefghijklmnopqrstuvwxyz"
        assertTrue("Valid xai- key should be accepted", MainActivity.isValidApiKey(key))
    }

    @Test
    fun testValidApiKey_caseInsensitive_SK() {
        // Valid: "SK-" (uppercase) should be accepted case-insensitively
        val key = "SK-abcdefghijklmnopqrstuvwxyz"
        assertTrue("SK- (uppercase) key should be accepted", MainActivity.isValidApiKey(key))
    }

    @Test
    fun testValidApiKey_caseInsensitive_XAI() {
        // Valid: "XAI-" (uppercase) should be accepted case-insensitively
        val key = "XAI-abcdefghijklmnopqrstuvwxyz"
        assertTrue("XAI- (uppercase) key should be accepted", MainActivity.isValidApiKey(key))
    }

    @Test
    fun testValidApiKey_caseInsensitive_Mixed() {
        // Valid: mixed case prefixes should be accepted
        assertTrue("Sk- (mixed case) should be accepted", MainActivity.isValidApiKey("Sk-abcdefghijklmnopqrstuvwxyz"))
        assertTrue("sK- (mixed case) should be accepted", MainActivity.isValidApiKey("sK-abcdefghijklmnopqrstuvwxyz"))
        assertTrue("Xai- (mixed case) should be accepted", MainActivity.isValidApiKey("Xai-abcdefghijklmnopqrstuvwxyz"))
        assertTrue("xAi- (mixed case) should be accepted", MainActivity.isValidApiKey("xAi-abcdefghijklmnopqrstuvwxyz"))
    }

    @Test
    fun testInvalidApiKey_missingHyphenAfterXai() {
        // Invalid: "xai" without hyphen should be rejected
        val key = "xaiabcdefghijklmnopqrstuvwxyz"
        assertFalse("Key with 'xai' but no hyphen should be rejected", MainActivity.isValidApiKey(key))
    }

    @Test
    fun testInvalidApiKey_tooShort() {
        // Invalid: "sk-short" is too short (< 20 characters)
        val key = "sk-short"
        assertFalse("Key shorter than MIN_API_KEY_LENGTH should be rejected", MainActivity.isValidApiKey(key))
    }

    @Test
    fun testInvalidApiKey_tooShort_xai() {
        // Invalid: "xai-" prefix but too short
        val key = "xai-short"
        assertFalse("xai- key shorter than MIN_API_KEY_LENGTH should be rejected", MainActivity.isValidApiKey(key))
    }

    @Test
    fun testValidApiKey_exactlyMinLength() {
        // Boundary: key exactly MIN_API_KEY_LENGTH (20) should be accepted
        val key = "sk-12345678901234567" // exactly 20 characters
        assertEquals("Test key should be exactly 20 characters", 20, key.length)
        assertTrue("Key with exactly MIN_API_KEY_LENGTH should be accepted", MainActivity.isValidApiKey(key))
    }

    @Test
    fun testValidApiKey_exactlyMinLength_xai() {
        // Boundary: xai- key exactly MIN_API_KEY_LENGTH (20) should be accepted
        val key = "xai-1234567890123456" // exactly 20 characters
        assertEquals("Test key should be exactly 20 characters", 20, key.length)
        assertTrue("xai- key with exactly MIN_API_KEY_LENGTH should be accepted", MainActivity.isValidApiKey(key))
    }

    @Test
    fun testInvalidApiKey_oneLessThanMinLength() {
        // Boundary: key with length 19 (one less than MIN) should be rejected
        val key = "sk-1234567890123456" // 19 characters
        assertEquals("Test key should be exactly 19 characters", 19, key.length)
        assertFalse("Key with length < MIN_API_KEY_LENGTH should be rejected", MainActivity.isValidApiKey(key))
    }

    @Test
    fun testInvalidApiKey_wrongPrefix() {
        // Invalid: wrong prefix should be rejected
        val key = "invalid-prefix12345678901234567890"
        assertFalse("Key with wrong prefix should be rejected", MainActivity.isValidApiKey(key))
    }

    @Test
    fun testInvalidApiKey_emptyString() {
        // Invalid: empty string should be rejected
        assertFalse("Empty string should be rejected", MainActivity.isValidApiKey(""))
    }

    @Test
    fun testInvalidApiKey_onlyPrefix() {
        // Invalid: only prefix without sufficient content
        assertFalse("Only 'sk-' should be rejected", MainActivity.isValidApiKey("sk-"))
        assertFalse("Only 'xai-' should be rejected", MainActivity.isValidApiKey("xai-"))
    }
}
