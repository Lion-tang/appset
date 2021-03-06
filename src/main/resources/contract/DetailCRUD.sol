pragma solidity>=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;

import "./Table.sol";


contract DetailCRUD {
    

    event InsertResult(int256 count);
    

    TableFactory tableFactory;
    string constant TABLE_name = "goods";
    constructor() public {
        tableFactory = TableFactory(0x1001); //The fixed address is 0x1001 for TableFactory
        // the parameters of createTable are tableuid,keyField,"vlaueFiled1,vlaueFiled2,vlaueFiled3,..."
        tableFactory.createTable(TABLE_name, "uid", "attr");
    }

    //select records
    function select(string memory uid)
    public
    view
    returns (string[] memory, string[] memory)
    {
        Table table = tableFactory.openTable(TABLE_name);

        Condition condition = table.newCondition();

        Entries entries = table.select(uid, condition);
        string[] memory goods_uid_bytes_list = new string[](uint256(entries.size()));
        string[] memory attr_list = new string[](uint256(entries.size()));
      

        for (int256 i = 0; i < entries.size(); ++i) {
            Entry entry = entries.get(i);
            goods_uid_bytes_list[uint256(i)] = entry.getString("uid");
            attr_list[uint256(i)] = entry.getString("attr");
        }

        return (goods_uid_bytes_list, attr_list);
    }
    
     //insert records
    function insert(string memory uid, string memory attr)
    public
    returns (int256)
    {
        Table table = tableFactory.openTable(TABLE_name);

        Entry entry = table.newEntry();
        entry.set("uid", uid);
        entry.set("attr", attr);
        int256 count = table.insert(uid, entry);
        emit InsertResult(count);

        return count;
    }
   
    
   
}