/*
 * Copyright (c) 2022 Tander, All Rights Reserved.
 */

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Класс Main
 */
public class Main {


	public static void main(String[] args) {
		List<String> lines = readFile("D:\\Developer\\my_projects\\string_matcher\\src\\main\\resources\\input.txt");
		List<List<String>> see = analyseLines(lines);
		List<String> resultList = matchLines(see.get(0), see.get(1));
		writeLines(resultList);

	}

	public static List<String> readFile(String path) {
		List<String> lines = new ArrayList<>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(path));
			while (scanner.hasNextLine()) {
				lines.add(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return lines;
	}

	public static List<List<String>> analyseLines(List<String> lines) {

		List<List<String>> resultList = new ArrayList<>();
		int elementsSize = Integer.parseInt(lines.get(0));
		resultList.add(new ArrayList<>(lines.subList(1, elementsSize + 1)));
		resultList.add(new ArrayList<>(lines.subList(elementsSize + 2,
			2 + elementsSize + Integer.parseInt(lines.get(elementsSize + 1)))));
		return resultList;
	}

	public static List<String> matchLines(List<String> firstList, List<String> secondList) {

		List<String> resultList = new ArrayList<>();
		List<String> currentFirst;
		List<String> currentSecond;
		if (firstList.size() > secondList.size()) {
			currentFirst = new ArrayList<>(secondList);
			currentSecond = new ArrayList<>(firstList);
		} else {
			currentFirst = new ArrayList<>(firstList);
			currentSecond = new ArrayList<>(secondList);
		}

		for (String str : currentFirst) {
			int size = 10000;
			String strResult = "";

			for (String str1 : currentSecond) {
				int result = getLevenshteinDistance(str, str1);

				if (size > result) {
					size = result;
					strResult = str1;

				}

			}
			currentSecond.remove(strResult);
			resultList.add(str + ":" + strResult);
		}
		resultList.addAll(currentSecond.stream().map(s -> s + ":?").collect(Collectors.toList()));
		return resultList;
	}

	public static int getLevenshteinDistance(CharSequence first, CharSequence second) {
		if (first == null || second == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}

		int firstLength = first.length();
		int secondLength = second.length();

		if (firstLength == 0) {
			return secondLength;
		} else if (secondLength == 0) {
			return firstLength;
		}

		if (firstLength > secondLength) {
			final CharSequence sequence = first;
			first = second;
			second = sequence;
			firstLength = secondLength;
			secondLength = second.length();
		}

		final int[] index = new int[firstLength + 1];
		int i;
		int j;
		int upper_left;
		int upper;
		char t_j;
		int cost;

		for (i = 0; i <= firstLength; i++) {
			index[i] = i;
		}

		for (j = 1; j <= secondLength; j++) {
			upper_left = index[0];
			t_j = second.charAt(j - 1);
			index[0] = j;

			for (i = 1; i <= firstLength; i++) {
				upper = index[i];
				cost = first.charAt(i - 1) == t_j ? 0 : 1;
				index[i] = Math.min(Math.min(index[i - 1] + 1, index[i] + 1), upper_left + cost);
				upper_left = upper;
			}
		}

		return index[firstLength];
	}

	public static void writeLines(List<String> list) {
		try (FileWriter writer = new FileWriter("output.txt", false)) {
			writer.write(String.valueOf(list));
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


