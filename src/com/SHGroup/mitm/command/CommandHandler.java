package com.SHGroup.mitm.command;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.SHGroup.mitm.Main;
import com.SHGroup.mitm.command.commands.CommandExit;

public class CommandHandler {
	public CommandHandler() {
	}

	private final Map<String, CommandExecutor> commands = new HashMap<>();

	public void initialize() {
		registerCommand("exit", "exit", CommandExit.class);
	}

	public void onExit() {

	}

	public void registerCommand(String name, String command, Class<? extends CommandExecutor> executor) {
		if (name == null || name.length() == 0) {
			Main.gui.appendLog("Error on register command. : name is null");
			return;
		}
		if (command == null || command.length() == 0) {
			Main.gui.appendLog("Error on register command. : command is null");
			return;
		}
		if (executor == null) {
			Main.gui.appendLog("Error on register command. : executor is null");
			return;
		}

		try {
			CommandExecutor commandexec = executor.newInstance();

			Field f1 = CommandExecutor.class.getDeclaredField("name");
			f1.setAccessible(true);
			f1.set(commandexec, name);

			Field f2 = CommandExecutor.class.getDeclaredField("command");
			f2.setAccessible(true);
			f2.set(commandexec, command);

			commands.put(command, commandexec);

			Main.gui.appendLog("Register Command Success! : " + command);
		} catch (Exception e) {
			e.printStackTrace();
			Main.gui.appendLog("Error on register command..");
			return;
		}
	}

	public void dispatchCommand(String text) {
		if (text == null || text.length() == 0) {
			Main.gui.appendLog("Error on dispatch command. : execute text is null");
			return;
		}
		String[] args = null;
		try {
			args = getArgs(text.toLowerCase());
		} catch (InvalidTokenizeException ex) {
			Main.gui.appendLog("Error on dispatch command. : " + ex.getMessage());
			return;
		}
		if (commands.containsKey(args[0])) {
			CommandExecutor command = commands.get(args[0]);
			command.execute(args);
		} else {
			Main.gui.appendLog("Error on dispatch command. : cannot found '" + args[0] + "' command");
		}
	}

	private final String[] getArgs(String command) throws InvalidTokenizeException {
		ArrayList<String> args = new ArrayList<String>();

		StringBuilder temp = new StringBuilder();
		char[] split = command.toCharArray();
		char opened = '\0';

		for (int i = 0; i < split.length; i++) {
			if (split[i] == '\\') {
				if (i + 1 == split.length) {
					throw new InvalidTokenizeException("Invalid token : need character after '\\'");
				}
				if (split[i + 1] == ' ' || split[i + 1] == '	' || split[i + 1] == '\\' || split[i + 1] == '"'
						|| split[i + 1] == '\'') {
					temp.append(split[i + 1]);
					i += 1;
				} else {
					throw new InvalidTokenizeException("Invalid token : '\\" + split[i + 1] + "'");
				}
			} else if (split[i] == '"' || split[i] == '\'') {
				if (opened == '\0') {
					opened = split[i];
				} else {
					if (split[i] != opened) {
						temp.append(split[i]);
					} else {
						args.add(temp.toString());
						temp = new StringBuilder();
						opened = '\0';
					}
				}
			} else if (split[i] == ' ' || split[i] == '	') {
				if (opened != '\0') {
					temp.append(split[i]);
					continue;
				}
				if (temp.length() > 0) {
					args.add(temp.toString());
					temp = new StringBuilder();
				}
			} else {
				temp.append(split[i]);
			}
		}

		if (temp.length() != 0) {
			args.add(temp.toString());
		}

		return args.toArray(new String[] {});
	}
}
