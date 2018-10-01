package com.excelsiormc.excelsiorsponge.commands;

import com.excelsiormc.excelsiorsponge.ExcelsiorSponge;
import com.excelsiormc.excelsiorsponge.excelsiorcore.services.text.Messager;
import com.excelsiormc.excelsiorsponge.game.match.Arena;
import com.excelsiormc.excelsiorsponge.game.match.Team;
import com.excelsiormc.excelsiorsponge.game.match.field.Grid;
import com.excelsiormc.excelsiorsponge.game.match.field.GridNormal;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.TerrainBuild;
import com.excelsiormc.excelsiorsponge.game.match.field.cells.CellTerrains;
import com.excelsiormc.excelsiorsponge.game.match.gamemodes.Gamemode;
import com.excelsiormc.excelsiorsponge.game.match.gamemodes.GamemodeDuel;
import com.excelsiormc.excelsiorsponge.game.match.matchmaking.Queues;
import com.excelsiormc.excelsiorsponge.game.match.profiles.CombatantProfilePlayer;
import com.excelsiormc.excelsiorsponge.game.user.UserPlayer;
import com.excelsiormc.excelsiorsponge.utils.PlayerUtils;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

import java.util.Optional;

public class ArenaCommands implements CommandExecutor {

    public ArenaCommands() {

        //parent command spec
        CommandSpec commandSpec = CommandSpec.builder()
                .description(Text.of("Arena commands"))
                .executor(this)
                .arguments(GenericArguments.string(Text.of("mode")),
                        GenericArguments.optional(GenericArguments.string(Text.of("data"))),
                        GenericArguments.optional(GenericArguments.string(Text.of("data1"))),
                        GenericArguments.optional(GenericArguments.string(Text.of("data2")))
                )
                .build();

        Sponge.getCommandManager().register(ExcelsiorSponge.INSTANCE, commandSpec, "arena");
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {

        Player player = (Player)commandSource;

        String mode = args.<String>getOne("mode").get();
        if(mode.equalsIgnoreCase("add")){
            try {
                int rowCount = Integer.valueOf(args.<String>getOne("data").get());
                int rowLength = Integer.valueOf(args.<String>getOne("data1").get());
                int cellDem = Integer.valueOf(args.<String>getOne("data2").get());

                Vector3d start = player.getLocation().getPosition();
                String world = player.getLocation().getExtent().getName();

                ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().add(new Arena(new GridNormal(start, world, rowCount, rowLength, cellDem, false), world));

            } catch (NullPointerException e){
                Messager.sendMessage(player, Text.of(TextColors.RED, "use /arena <grid x> <grid z> <cell x> <cell z>"), Messager.Prefix.ERROR);
            }

        } else if(mode.equalsIgnoreCase("start")){
            Optional<Arena> arena = ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().getAvailableArena();
            if(!arena.isPresent()){
                Messager.sendMessage(player, Text.of(TextColors.RED, "No arena is available. Tell staff"), Messager.Prefix.ERROR);
            } else {
                UserPlayer userPlayer = PlayerUtils.getUserPlayer(player.getUniqueId()).get();
                Gamemode gamemode = new GamemodeDuel(arena.get().getWorld());
                gamemode.addTeam(new Team(new CombatantProfilePlayer(player.getUniqueId(), userPlayer.getDeck())));
                arena.get().start(gamemode);
            }
        } else if(mode.equalsIgnoreCase("gen")){
            try {
                int rowCount = Integer.valueOf(args.<String>getOne("data").get());
                int rowLength = Integer.valueOf(args.<String>getOne("data1").get());
                int cellDem = Integer.valueOf(args.<String>getOne("data2").get());

                Vector3d start = player.getLocation().getPosition();
                String world = player.getLocation().getExtent().getName();

                ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager()
                        .add(new Arena(new GridNormal(start, world, rowCount, rowLength, cellDem, true), world));

            } catch (NullPointerException e){
                Messager.sendMessage(player, Text.of(TextColors.RED, "use /arena <grid x> <grid z> <cell x> <cell z>"), Messager.Prefix.ERROR);
            }

        } else if(mode.equalsIgnoreCase("join")){
            ExcelsiorSponge.INSTANCE.getMatchMaker().playerJoinQueue(player, Queues.DUEL);
        } else if(mode.equalsIgnoreCase("tgen")){
            Grid grid = ExcelsiorSponge.INSTANCE.getMatchMaker().getArenaManager().findArenaWithCombatant(player.getUniqueId()).get().getGrid();
            grid.resetCells();
            grid.generateTerrain();
        } else if(mode.equalsIgnoreCase("build")){
            int xzDim = Integer.valueOf(args.<String>getOne("data").get());
            int yDim = Integer.valueOf(args.<String>getOne("data1").get());

            CellTerrains type = CellTerrains.valueOf(args.<String>getOne("data2").get().toUpperCase());
            Vector3d start = player.getLocation().getPosition().clone().add(1, 0, 1);
            Vector3d end = start.clone().add(xzDim, yDim, xzDim);

            type.getCellType().setBuild(new TerrainBuild(new Location(player.getWorld(), start), new Location(player.getWorld(), end)));
        }

        return CommandResult.success();
    }
}
