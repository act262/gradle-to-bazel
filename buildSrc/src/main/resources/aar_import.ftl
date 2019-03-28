
aar_import(
    name = '$aarName',
    aar = '$aarTarget',
    exports = [
        #foreach($item in $aarExports)
        '$item',
        #end
    ]
)
