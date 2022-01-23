package net.kemitix.trello;

import com.julienvey.trello.NotFoundException;
import com.julienvey.trello.Trello;
import com.julienvey.trello.domain.*;
import com.julienvey.trello.domain.Attachment;
import lombok.extern.java.Log;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.kemitix.trello.ListUtils.map;

@Log
public class TrelloBoard {

    private final Trello trello;
    private final TrelloConfig trelloConfig;

    private List<TList> lists;

    public TrelloBoard(
            Trello trello,
            TrelloConfig trelloConfig
    ) {
        this.trello = trello;
        this.trelloConfig = trelloConfig;
    }

    public void init () {
        lists = board().fetchLists();
    }

    private Board board() {
        String userName = trelloConfig.getUserName();
        log.info("User: " + userName);
        String boardName = trelloConfig.getBoardName();
        log.info("Loading Board: " + boardName);
        return trello
                .getMemberBoards(userName)
                .stream()
                .filter(board -> board.getName().equals(boardName))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Board: " + boardName));
    }

    private TList getList(String listName) {
        return lists
                .stream()
                .filter(list -> list.getName().equals(listName))
                .findAny()
                .orElseThrow(() -> new NotFoundException("List: " + listName));
    }


    public void updateCard(TrelloCard card) {
        trello.updateCard(card);
    }

    public List<TrelloCard> getListCards(String listName) {
        return trello.getListCards(getListId(listName)).stream()
                .map(card -> TrelloCard.from(card, trello))
                .collect(Collectors.toList());
    }

    public List<Attachment> getAttachments(Card card) {
        return trello.getCardAttachments(card.getId());
    }

    public TrelloCard addMemberToCard(TrelloCard card, Member member) {
        var members = trello.addMemberToCard(card.getId(), member.getId());
        card.setIdMembers(map(members, Member::getId));
        trello.updateCard(card);
        return card;
    }

    public TrelloCard removeMemberFromCard(TrelloCard card, Member member) {
        var members = trello.removeMemberFromCard(card.getId(), member.getId());
        card.setIdMembers(map(members, Member::getId));
        trello.updateCard(card);
        return card;
    }

    public String getListId(String listName) {
        return getList(listName).getId();
    }

    public String getBoardId() {
        return board().getId();
    }

    public Stream<String> getListNames() {
        return lists.stream()
                .map(TList::getName);
    }

    public TrelloCard getCard(String cardId) {
        Card card = trello.getCard(cardId);
        return TrelloCard.from(card, trello);
    }
}
