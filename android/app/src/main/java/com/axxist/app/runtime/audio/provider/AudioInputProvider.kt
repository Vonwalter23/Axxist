package com.axxist.app.runtime.audio.provider

/**
 * AudioInputProvider - Abstraction for audio input sources.
 * 
 * This interface defines the contract for audio capture.
 * Implementations can include:
 * - Device microphone
 * - Audio files
 * - Streaming sources
 * - Test/mock sources
 */
interface AudioInputProvider {
    
    /**
     * Audio configuration for capture.
     */
    data class AudioConfig(
        val sampleRate: Int = 16000,
        val channelConfig: ChannelConfig = ChannelConfig.MONO,
        val audioFormat: AudioFormat = AudioFormat.PCM_16BIT,
        val bufferSize: Int = 4096
    )
    
    enum class ChannelConfig {
        MONO,
        STEREO
    }
    
    enum class AudioFormat {
        PCM_16BIT,
        PCM_8BIT,
        PCM_FLOAT
    }
    
    /**
     * Initialize the audio input provider.
     */
    suspend fun initialize(config: AudioConfig): Boolean
    
    /**
     * Start capturing audio.
     */
    suspend fun startCapture(): Boolean
    
    /**
     * Stop capturing audio.
     */
    suspend fun stopCapture(): Boolean
    
    /**
     * Check if currently capturing.
     */
    fun isCapturing(): Boolean
    
    /**
     * Read captured audio data.
     * Returns null if no data available.
     */
    suspend fun read(): ByteArray?
    
    /**
     * Set audio data listener.
     */
    fun setListener(listener: AudioDataListener?)
    
    /**
     * Release resources.
     */
    suspend fun release()
    
    /**
     * Check if provider is available.
     */
    fun isAvailable(): Boolean
    
    /**
     * Listener interface for audio data.
     */
    interface AudioDataListener {
        fun onAudioData(data: ByteArray)
        fun onError(error: String)
    }
}
