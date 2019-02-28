/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NLPAlgorithms;

import ToolsDL.LabelSeeker;
import ToolsDL.MeansBuilder;
import java.io.IOException;
import java.util.List;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.text.documentiterator.FileLabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.primitives.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Punkid PC
 */
public class ParagraphVectorsClassifier {
    
    ParagraphVectors  paragraphVectors;
    LabelAwareIterator iterator;
    TokenizerFactory tokenizerFactory;
    
    private static final Logger log = LoggerFactory.getLogger(ParagraphVectorsClassifier.class);
    
 public static void main(String[] args) throws Exception {

      ParagraphVectorsClassifier app = new ParagraphVectorsClassifier();
      app.makeParagraphVectors();
      app.checkUnlabeledData();

    }

    void makeParagraphVectors()  throws Exception {
      ClassPathResource resource = new ClassPathResource("data/labeled");
      System.out.println(resource.getURL().toString());

      // build a iterator for our dataset
      iterator = new FileLabelAwareIterator.Builder()
              .addSourceFolder(resource.getFile())
              .build();

      tokenizerFactory = new DefaultTokenizerFactory();
      tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

      // ParagraphVectors training configuration
      paragraphVectors = new ParagraphVectors.Builder()
              .learningRate(0.025)
              .minLearningRate(0.001)
              .batchSize(1000)
              .epochs(20)
              .iterate(iterator)
              .trainWordVectors(true)
              .tokenizerFactory(tokenizerFactory)
              .build();

      // Start model training
      paragraphVectors.fit();
    }

    void checkUnlabeledData() throws IOException {
      /*
      At this point we assume that we have model built and we can check
      which categories our unlabeled document falls into.
      So we'll start loading our unlabeled documents and checking them
     */
     ClassPathResource unClassifiedResource = new ClassPathResource("data/unlabeled");
     FileLabelAwareIterator unClassifiedIterator = new FileLabelAwareIterator.Builder()
             .addSourceFolder(unClassifiedResource.getFile())
             .build();

     /*
      Now we'll iterate over unlabeled data, and check which label it could be assigned to
      Please note: for many domains it's normal to have 1 document fall into few labels at once,
      with different "weight" for each.
     */
     MeansBuilder meansBuilder = new MeansBuilder(
         (InMemoryLookupTable<VocabWord>)paragraphVectors.getLookupTable(),
           tokenizerFactory);
     LabelSeeker seeker = new LabelSeeker(iterator.getLabelsSource().getLabels(),
         (InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable());

     while (unClassifiedIterator.hasNextDocument()) {
         LabelledDocument document = unClassifiedIterator.nextDocument();
         INDArray documentAsCentroid = meansBuilder.documentAsVector(document);
         List<Pair<String, Double>> scores = seeker.getScores(documentAsCentroid);

         /*
          please note, document.getLabel() is used just to show which document we're looking at now,
          as a substitute for printing out the whole document name.
          So, labels on these two documents are used like titles,
          just to visualize our classification done properly
         */
         log.info("Document '" + document.getLabels() + "' falls into the following categories: ");
         for (Pair<String, Double> score: scores) {
             log.info("        " + score.getFirst() + ": " + score.getSecond());
         }
     }

    }
}
