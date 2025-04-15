package com.hexvoid.nexusjpa.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ThreadRun {

	public static void main(String[] args) {

		// Sample input list
		List<Integer> numbers = Arrays.asList(2, 15, 118, 56, 818, 11, 1, 11, 818, 56);

		// Removing duplicates using distinct()
		List<Integer> distinct = numbers.stream()
				.distinct()
				.collect(Collectors.toList());
		System.out.println(distinct);

		// Filtering numbers that start with '1'
		List<Integer> startingWithOne = numbers.stream()
				.filter(num -> String.valueOf(num).startsWith("1"))
				.collect(Collectors.toList());
		System.out.println(startingWithOne);

		// Reversing a string using StringBuilder
		String original = "WORLD IS A BETTER PLACE";
		StringBuilder reversed = new StringBuilder();
		for (int i = original.length() - 1; i >= 0; i--) {
			reversed.append(original.charAt(i));
		}
		System.out.println(reversed);

		// Producer-consumer implementation using wait/notify

		ProducerConsumer sharedResource = new ProducerConsumer();  // Shared object between producer and consumer threads

		// ✅ Producer thread defined using Anonymous Inner Class (classic way)
		Runnable produce = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					sharedResource.produce(i);  // Producing values 0 to 9
				}
			}
		};

		// Thread initialized with producer task and started
		Thread producerThread = new Thread(produce);
		producerThread.start();

		// ✅ Consumer thread defined using Lambda Expression (modern, concise way)
		Thread consumerThread = new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				sharedResource.consume(i);  // Consuming values 0 to 9
			}
		});
		consumerThread.start();

	}

}
