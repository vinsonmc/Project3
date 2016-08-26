package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Vinson on 8/26/2016.
 */
public class UserTest {
    private Board board;
    private User questioner;
    private User answerer;
    private User upVoter;
    private Question question;
    private Answer answer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        board = new Board("testTopic");
        questioner = board.createUser("questioner");
        answerer = board.createUser("answerer");
        upVoter = board.createUser("upvoter");
        question = questioner.askQuestion("Questioner's question");
        answer = answerer.answerQuestion(question, "Answer to question");
    }
    @Test
    public void questionerReputationIncreasesWhenQuestionIsUpvoted() throws Exception {
        upVoter.upVote(question);

        assertEquals(5, questioner.getReputation());
    }

    @Test
    public void answererReputationIncreasesWhenAnswerIsUpvoted() throws Exception {
        upVoter.upVote(answer);

        assertEquals(10, answerer.getReputation());
    }

    @Test
    public void answererReputationIncreasesWhenAnswerIsAccepted() throws Exception {
        questioner.acceptAnswer(answer);

        assertEquals(15, answerer.getReputation());
    }

    @Test
    public void upvotingByAuthorNotAllowed() throws Exception {
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");

        questioner.upVote(question);
        answerer.upVote(answer);
    }

    @Test
    public void onlyQuestionerCanAcceptAnswer() throws Exception {
        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage(String.format("Only %s can accept this answer as it is their question",
                questioner.getName()));

        answerer.acceptAnswer(answer);
        upVoter.acceptAnswer(answer);
    }

    @Test
    public void userWithNoUpvotesReturns0Reputation() throws Exception {
        assertEquals(0, questioner.getReputation());
    }


}