package ru.strid.strreward.enums;

public enum RewardType {
    FULL_TABLE, //Выдает весь инвентарь зоны игроку
    ONE_ITEM_PER_PLAYER, //Выдает только ОДИН предмет из инвентаря зоны, после выдачи (ВОЗМОЖНО помечает предмет недоступным)
    SOME_ITEMS //Выдает некоторые предметы
}
