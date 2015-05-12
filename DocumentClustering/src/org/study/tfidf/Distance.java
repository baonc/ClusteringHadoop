package org.study.tfidf;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Lop tinh khoang cach cua hai document
 * 
 * @created 29 / 3 / 2015
 * @author baonc
 *
 */
public class Distance {
	private static final DecimalFormat DF = new DecimalFormat("###.########");

	/**
	 * Ham chuan hoa hai vector document
	 * Dua ve cung mot dimension
	 * 
	 * @param center la mang string[] khi ma vb dc tach thanh dang tu=tfidfWeight
	 * @param document
	 * @return ArrayList chua dimension cua hai document
	 */
	public static ArrayList<String> normalize(String center[], String document[]) {
		ArrayList<String> normalize = new ArrayList<String>();
		
		for(int i = 0; i < center.length; i++) {
			normalize.add(center[i].split("=")[0]);
		}

		for(int i = 0; i < document.length; i++) {
			if(!normalize.contains(document[i].split("=")[0])) {
				normalize.add(document[i].split("=")[0]);
			}
		}	
		
		return normalize;
	}

	/**
	 * Ham tinh khoang cach cua hai document
	 * 
	 * @param center
	 * @param document
	 * @return	khoang cach cua hai document theo eclide
	 */
	public static double distance(String center[], String document[]) {
		double dis = 0;
		double sum = 0;
		ArrayList<String> normalize = normalize(center, document);		
		Map<String, Double> centerHashMap = new HashMap<String, Double>();
		Map<String, Double> documentHashMap = new HashMap<String, Double>();
		
		// in ra dimension
		System.out.println("Dimension number: " + normalize.size());
		for(int i = 0; i < normalize.size(); i++) {
			System.out.println(normalize.get(i));
		}
		
		// chuyen dang du=tfidf sang dang hashmap key: tu, value: tfidf
		for(int i = 0; i < center.length; i++) {
			String wordAndTfidf[] = center[i].split("=");
			centerHashMap.put(wordAndTfidf[0], Double.parseDouble(wordAndTfidf[1]));
		}
		
		for(int i = 0; i < document.length; i++) {
			String wordAndTfidf[] = document[i].split("=");
			documentHashMap.put(wordAndTfidf[0], Double.parseDouble(wordAndTfidf[1]));
		}
		
		/**
		 * Voi moi tu trong tap chuan hoa:
		 * 	Kiem tra xem tu do co xuat hien trong hai tap center va document hay khong ?
		 * 		Neu hai HashMap cung xuat hien:	sum = binh phuong gia tri hieu cua hai tu
		 * 		Neu chi xuat hien mot trong hai:	sum = binh phuong gia tri cua hashmap xuat hien. 
		 */
		for(int i = 0; i < normalize.size(); i++) {
			String word = normalize.get(i);
			if(centerHashMap.containsKey(word) && documentHashMap.containsKey(word)) {
				sum += (centerHashMap.get(word) - documentHashMap.get(word)) * (centerHashMap.get(word) - documentHashMap.get(word));
			} else if(centerHashMap.containsKey(word) && !documentHashMap.containsKey(word)) {
				sum += centerHashMap.get(word) * centerHashMap.get(word);
			} else if(!centerHashMap.containsKey(word) && documentHashMap.containsKey(word)) {
				sum += documentHashMap.get(word) * documentHashMap.get(word);
			}
		} 
		
		dis = Math.sqrt(sum);
		
		return dis;
	}
	
	/**
	 * Tinh Vector trung binh giua cac document
	 * 
	 * @param doc[]: Tap cac doc
	 * @return Vector trung binh
	 */
	public static String[] avgDocument(String[]... doc){
		//Neu chi co mot VB thi tra ve VB ay
		if(doc.length == 1) {
			return doc[0];
		}

		// Chuan hoa tap document
		ArrayList<String> normalize = new ArrayList<String>();
		normalize = normalize(doc[0], doc[1]);
		for(int i = 2; i < doc.length; i++) {
			String normalizeArr[] = new String[normalize.size()];
			normalizeArr = normalize.toArray(normalizeArr);
			normalize = normalize(normalizeArr, doc[i]);
		}
		
		// Chuyen cac doc ve dang hashmap
		ArrayList<Map<String, Double>> listOfDocHashMap = new ArrayList<Map<String, Double>>();
		for(int i = 0; i < doc.length; i++) {
			Map<String, Double> docHashMap = new HashMap<String, Double>();
			for(int j = 0; j < doc[i].length; j++) {
				String wordAndTfidf[] = doc[i][j].split("=");
				docHashMap.put(wordAndTfidf[0], Double.parseDouble(wordAndTfidf[1]));
			}
			listOfDocHashMap.add(i, docHashMap);
		}
		
		// tinh vector trung binh
		String avgDocument[] = new String[normalize.size()];
		for(int i = 0; i < normalize.size(); i++) {
			String word = normalize.get(i);
			double avgTfidf = 0;
			for(int j = 0; j < listOfDocHashMap.size(); j++) {
				if(listOfDocHashMap.get(j).containsKey(word)) {
					avgTfidf += listOfDocHashMap.get(j).get(word);
				} else {
					avgTfidf += 0;
				}
			}
			avgTfidf /= doc.length;
			avgDocument[i] = word + "=" + avgTfidf;
		}
		
		return avgDocument;
	}
	
	/**
	 * Ham tinh vector trung binh cua 2 doc khi ma mot doc la empty
	 * 
	 * @param emptyDoc empty doc
	 * @param doc 
	 * @return vector trung binh
	 */
	public static String[] avgDocument(String emptyDoc, String doc[]) {
		return doc;
	}
	
	/**
	 * Ham kiem tra hai vector co trung nhau hay khong
	 * 
	 * @param oldCenter 
	 * @param newCenter
	 * @return true neu oldCenter == newCenter, false neu nguoc lai.
	 */
	public static boolean isEqual(String oldCenter[], String newCenter[]) {
		if(oldCenter.length != newCenter.length) {
			return false;
		} else {
			ArrayList<String> normalize = normalize(oldCenter, newCenter);
			Map<String, Double> oldCenterHashMap = new HashMap<String, Double>();
			Map<String, Double> newCenterHashMap = new HashMap<String, Double>();
			
			// chuyen ve dang hashmap
			for(int i = 0; i < oldCenter.length; i++) {
				String wordAndTfidfOldCenter[] = oldCenter[i].split("=");
				oldCenterHashMap.put(wordAndTfidfOldCenter[0], Double.parseDouble(wordAndTfidfOldCenter[1]));
				String wordAndTfidfNewCenter[] = newCenter[i].split("=");
				newCenterHashMap.put(wordAndTfidfNewCenter[0], Double.parseDouble(wordAndTfidfNewCenter[1]));
			}
			
			for(int i = 0; i < normalize.size(); i++) {
				String word = normalize.get(i);
				if(oldCenterHashMap.containsKey(word) && newCenterHashMap.containsKey(word)) {
					if(oldCenterHashMap.get(word) - newCenterHashMap.get(word) != 0) {
						return false;
					}
				} else if(oldCenterHashMap.containsKey(word) && !newCenterHashMap.containsKey(word)) {
					return false;
				} else if(!oldCenterHashMap.containsKey(word) && newCenterHashMap.containsKey(word)) {
					return false;
				}
			}
			
			return true;
		}
	}
	
	/**
	 * Ham tinh tong hai document
	 * 
	 * @param doc1: Vector tfidf cua doc1
	 * @param doc2: Vector tfidf cua doc2
	 * @return: Tong tfidf cua hai document
	 */
	public static String[] sum(String doc1[], String doc2[]) {
		// Neu nhu cluster chi co mot document
		if(doc2 == null) {
			return doc1;
		}
		if(doc1 == null) {
			return doc2;
		}
		
		// chuan hoa hai document
		ArrayList<String> normalize = normalize(doc1, doc2);
		
		// chuyen ve dang hashmap
		Map<String, Double> doc1HashMap = new HashMap<String, Double>();
		Map<String, Double> doc2HashMap = new HashMap<String, Double>();
		for(int i = 0; i < doc1.length; i++) {
			String wordAndTfidf[] = doc1[i].split("=");
			doc1HashMap.put(wordAndTfidf[0], Double.parseDouble(wordAndTfidf[1]));
		}
		for(int i = 0; i < doc2.length; i++) {
			String wordAndTfidf[] = doc2[i].split("=");
			doc2HashMap.put(wordAndTfidf[0], Double.parseDouble(wordAndTfidf[1]));
		}
		
		// Tinh tong cua hai document
		String sum[] = new String[normalize.size()];
		for(int i = 0; i < normalize.size(); i++) {
			String word = normalize.get(i);
			if(doc1HashMap.containsKey(word) && doc2HashMap.containsKey(word)) {
				sum[i] = word + "=" + DF.format(doc1HashMap.get(word) + doc2HashMap.get(word));
			} else if(doc1HashMap.containsKey(word) && !doc2HashMap.containsKey(word)) {
				sum[i] = word + "=" + DF.format(doc1HashMap.get(word));
			} else if(!doc1HashMap.containsKey(word) && doc2HashMap.containsKey(word)) {
				sum[i] = word + "=" + DF.format(doc2HashMap.get(word));
			}
		}
		
		return sum;
	}
	
	/**
	 * Ham tinh gia tri trung binh cua tap document
	 * 
	 * @param sumDoc
	 * @return Document trung binh
	 */
	public static String[] avgDocument(String sumDoc[]) {
		String avgDocument[] = new String[sumDoc.length];
		
		for(int i = 0; i < sumDoc.length; i++) {
			String wordAndTfidf[] = sumDoc[i].split("=");
			double avgWeight = Double.parseDouble(wordAndTfidf[1]) / sumDoc.length;
			avgDocument[i] = wordAndTfidf[0] + "=" + DF.format(avgWeight);
		}
		
		return avgDocument;
	}
}