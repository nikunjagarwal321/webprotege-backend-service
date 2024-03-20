package edu.stanford.protege.webprotege.crud.gen;

import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsByValueIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyID;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.IllegalFormatException;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IncrementingPatternDescriptorValueGenerator_TestCase {

    public static final int STARTING_VALUE = 9876;

    private IncrementingPatternDescriptorValueGenerator generator;

    private OWLDataFactory dataFactory = new OWLDataFactoryImpl();

    @Mock
    private AnnotationAssertionAxiomsByValueIndex annotationAssertionAxiomsByValueIndex;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OWLAnnotationProperty property;

    @Mock
    private IncrementingPatternDescriptor descriptor;

    @Mock
    private OWLAnnotationAssertionAxiom axiom;

    @Mock
    private OWLOntologyID ontologyId;

    @Before
    public void setUp() throws Exception {
        generator = new IncrementingPatternDescriptorValueGenerator(
                dataFactory,
                annotationAssertionAxiomsByValueIndex,
                projectOntologiesIndex
        );

        when(projectOntologiesIndex.getOntologyIds())
                .then(inv -> Stream.of(ontologyId));

        when(descriptor.getStartingValue())
                .thenReturn(STARTING_VALUE);
        when(descriptor.getFormat())
                .thenReturn("");

        when(axiom.getProperty())
                .thenReturn(property);

        // By default don't "contain" any axiomsSource
        when(annotationAssertionAxiomsByValueIndex.getAxiomsByValue(any(),
                                                                    any()))
                .thenAnswer(inv -> Stream.empty());
    }

    @Test
    public void shouldGenerateStartValue() {
        var value = generator.generateNextValue(property, descriptor);
        assertThat(value.getLiteral(), is("9876"));
        assertThat(value.getDatatype().isInteger(), is(true));
    }


    @Test
    public void shouldGenerateStartValueWithPattern() {
        when(descriptor.getFormat())
                .thenReturn("Hello %08d");
        var value = generator.generateNextValue(property, descriptor);
        assertThat(value.getLiteral(), is("Hello 00009876"));
        assertThat(value.getDatatype().isString(), is(true));
    }

    @Test(expected = IllegalFormatException.class)
    public void shouldThrowExceptionForInvalidPatternWithTooManyArguments() {
        when(descriptor.getFormat())
                .thenReturn("Hello %d %s");
        generator.generateNextValue(property, descriptor);
    }

    @Test(expected = IllegalFormatException.class)
    public void shouldThrowExceptionForMalformedPattern() {
        when(descriptor.getFormat())
                .thenReturn("Hello %1");
        generator.generateNextValue(property, descriptor);
    }



    @Test
    public void shouldGenerateNextAvailableValue() {
        when(annotationAssertionAxiomsByValueIndex.getAxiomsByValue(dataFactory.getOWLLiteral(STARTING_VALUE),
                                                                    ontologyId))
                .thenAnswer(inv -> Stream.of(axiom));
        var value = generator.generateNextValue(property, descriptor);
        assertThat(value.getLiteral(), is("9877"));
        assertThat(value.getDatatype().isInteger(), is(true));
    }
}