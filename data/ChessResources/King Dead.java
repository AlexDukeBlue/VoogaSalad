ArrayList<backend.unit.Unit> kings = new ArrayList<>();
kings.addAll(gameState.getActiveTeam().getOwnedUnits(gameState.getGrid()).stream().filter(unit -> unit.getName().contains(“King”)).collect(Collectors.toList()));

if(kings.size() == 0) return "LOSE";
return "NONE";