package com.lowang.ormquerydsl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.RingBuffer;

public class LongEventProducer {
  private static final Logger LOGGER = LoggerFactory.getLogger(LongEventProducer.class);
  private final RingBuffer<LongEvent> ringBuffer;

  public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
    this.ringBuffer = ringBuffer;
  }

  public void onData(long bb) {

    long current = ringBuffer.getCursor();
    LOGGER.info("current {}", current);

    long sequence = ringBuffer.next(); // Grab the next sequence
    LOGGER.info("next {}", sequence);
    try {
      LongEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
      // for the sequence
      // LOGGER.info("product=[{}]",bb);
      event.set(bb); // Fill with data、
    } finally {
      ringBuffer.publish(sequence);
    }
  }
}
