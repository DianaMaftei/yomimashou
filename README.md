# yomimashou
A reading aid for Japanese.

Consider this: you want to read a Japanese text online but you're not yet proficient enough to understand it all. So you might install something like rikaichan/rikaikun to help you get fast definitions of words/kanji. All well and good. But what about after 5 minutes of trying to piece together the words from a sentence you still don't understand it fully? You might copy-paste it into google translate, see if you get lucky there.

How about when you encounter an interesting kanji that you don't know and are curious about the kanji stroke? You might copy paste that into wwwdict or jisho to see an animation of it. 

And if you want to hear the text being read aloud? You might copy-paste it into a website or install a browser exension. And if you want to save the words to practice later with your favorite RSS software/website? You have to copy-paste some more.

Do you see where I'm going with this? What if you could have all this and more in one single place? No more copy-pasting, no more installing n brower extensions. Yomimashou tries to do exactly this. Solve all those "if"s for you.


####Prerequisites: 
At least jdk1.8.0_151 - older versions might throw errors when unmarshalling the xml files

####How to run:
Server:
1. run fetchDictionariesScript.sh in resources/dictionaryXMLData
2. uncomment in application.properties #spring.jpa.hibernate.ddl-auto=create
3. uncomment in App.java  //dictionaryXMLtoPOJO.run();
4. run App.java

Client:
1. npm install 
2. npm start