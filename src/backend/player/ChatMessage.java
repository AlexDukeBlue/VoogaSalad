package backend.player;

import backend.util.GameplayState;
import controller.GameplayModifierBuilder;
import util.net.Modifier;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * @author Created by th174 on 4/9/2017.
 */
public final class ChatMessage {
	private final ImmutablePlayer sender;
	private final Instant timeStamp;
	private final AccessLevel accessLevel;
	private final String content;

	public ChatMessage(AccessLevel accessLevel, ImmutablePlayer sender, String content) {
		this.sender = sender;
		this.timeStamp = Instant.now(Clock.systemUTC());
		this.accessLevel = accessLevel;
		this.content = content;
	}

	public ImmutablePlayer getSender() {
		return sender;
	}

	public String getContent() {
		return content;
	}

	public AccessLevel getAccessLevel() {
		return accessLevel;
	}

	public Instant getTimeStamp() {
		return timeStamp;
	}

	@Override
	public String toString() {
		return String.format("  --%s--\n%s %s: %s", getTimeStamp().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)), "[" + getAccessLevel() + "]", getSender().getName(), getContent());
	}

	public enum AccessLevel {
		WHISPER(args -> state -> state.messagePlayer((String) args[0], state.getPlayerByName((String) args[1]), state.getPlayerByName((String) args[2]))),
		TEAM(args -> state -> state.messageTeam((String) args[0], state.getPlayerByName((String) args[1]))),
		ALL(args -> state -> state.messageAll((String) args[0], state.getPlayerByName((String) args[1])));

		private final GameplayModifierBuilder gameplayModifierBuilder;

		AccessLevel(GameplayModifierBuilder gameplayModifierBuilder) {
			this.gameplayModifierBuilder = gameplayModifierBuilder;
		}

		public Modifier<GameplayState> getSendMessageModifier(String message, String senderName) {
			return getSendMessageModifier(message, senderName, null);
		}

		public Modifier<GameplayState> getSendMessageModifier(String message, String senderName, String recipientName) {
			return gameplayModifierBuilder.buildFrom(message, senderName, recipientName);
		}

		@Override
		public String toString() {
			return super.toString();
		}
	}
}
