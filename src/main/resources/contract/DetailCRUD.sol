pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "./Table.sol";


contract DetailCRUD {
    
    event CreateResult(int256 count);
    event InsertResult(int256 count);
    event UpdateResult(int256 count);
    event RemoveResult(int256 count);

    TableFactory tableFactory;
    string constant TABLE_name = "goods";
    constructor() public {
        tableFactory = TableFactory(0x1001); //The fixed address is 0x1001 for TableFactory
        // the parameters of createTable are tableuid,keyField,"vlaueFiled1,vlaueFiled2,vlaueFiled3,..."
        tableFactory.createTable(TABLE_name, "uid", "hash,description");
    }

    //select records
    function select(string memory uid)
    public
    view
    returns (string[] memory, string[] memory, string[] memory)
    {
        Table table = tableFactory.openTable(TABLE_name);

        Condition condition = table.newCondition();

        Entries entries = table.select(uid, condition);
        string[] memory goods_uid_bytes_list = new string[](uint256(entries.size()));
        string[] memory hash_list = new string[](uint256(entries.size()));
        string[] memory description_list = new string[](uint256(entries.size()));

        for (int256 i = 0; i < entries.size(); ++i) {
            Entry entry = entries.get(i);

            goods_uid_bytes_list[uint256(i)] = entry.getString("uid");
            hash_list[uint256(i)] = entry.getString("hash");
            description_list[uint256(i)] = entry.getString("description");
            
        }

        return (goods_uid_bytes_list, hash_list, description_list);
    }
    
     //insert records
    function insert(string memory uid, string memory hash, string memory description)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_name);

        Entry entry = table.newEntry();
        entry.set("uid", uid);
        entry.set("hash", hash);
        entry.set("description", description);

        int256 count = table.insert(uid, entry);
        emit InsertResult(count);

        return count;
    }
    //update records
    function update(string memory uid, string memory hash, string memory description)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_name);

        Entry entry = table.newEntry();
        entry.set("hash", hash);
        entry.set("description", description);

        Condition condition = table.newCondition();
        condition.EQ("uid", uid);

        int256 count = table.update(uid, entry, condition);
        emit UpdateResult(count);

        return count;
    }
    //remove records
    function remove(string memory uid) public returns (int256) {
        Table table = tableFactory.openTable(TABLE_name);

        Condition condition = table.newCondition();
        condition.EQ("uid", uid);

        int256 count = table.remove(uid, condition);
        emit RemoveResult(count);

        return count;
    }
    
   
}