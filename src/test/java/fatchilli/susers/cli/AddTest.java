package fatchilli.susers.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.approvaltests.Approvals;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import fatchilli.susers.susers.Suser;
import fatchilli.susers.susers.SuserAlreadyExistsException;
import fatchilli.susers.susers.SuserDao;

class AddTest {

    @Test
    void acceptCorrectLine() throws Exception {
        final Stream<String> inputs = Files.lines(Path.of(this.getClass().getResource("/add.correct.input").toURI()));
        List<Suser> result = new ArrayList<>();
        Assertions.assertThatNoException()
                .isThrownBy(() -> {
                    final SuserDao stub = new SuserDao() {
                        @Override
                        public void add(Suser suser) throws SuserAlreadyExistsException {
                            result.add(suser);
                        }

                        @Override
                        public void deleteSusers() {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public List<Suser> getSusers() {
                            throw new UnsupportedOperationException();
                        }
                    };
                    final Add addCommand = new Add(stub);
                    inputs.forEach(input -> {
                        assertThat(addCommand.doesAcceptLine(input)).isTrue()
                                .describedAs("Add command has to accept before processing");
                        addCommand.processLine(input);
                    });

                });
        Approvals.verifyAll("Resulting users", result);
    }

    @Test
    void failsOnIncorrectFormat() throws Exception {
        final Stream<String> inputs = Files.lines(Path.of(this.getClass().getResource("/add.incorrect.input").toURI()));
        final Add add = new Add(new SuserDao() {
            @Override
            public void add(Suser suser) throws SuserAlreadyExistsException {

            }

            @Override
            public void deleteSusers() {

            }

            @Override
            public List<Suser> getSusers() {
                return null;
            }
        });
        inputs.forEach(input -> Assertions.assertThatExceptionOfType(CommandParseException.class)
                .isThrownBy(() -> add.processLine(input)));
    }

    @Test
    void ignoringOtherCommand() {
        final Add add = new Add(new SuserDao() {
            @Override
            public void add(Suser suser) throws SuserAlreadyExistsException {

            }

            @Override
            public void deleteSusers() {

            }

            @Override
            public List<Suser> getSusers() {
                return null;
            }
        });

        assertThat(add.doesAcceptLine("PrintAll"))
                .isFalse();

    }
}