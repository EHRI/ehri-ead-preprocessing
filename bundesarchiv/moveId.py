import sys
import xml.etree.ElementTree as ET

NS = 'urn:isbn:1-931666-22-9'
EMI = 'ehri_main_identifier'
TP = 'call number'
EA = '3.1.1'
ET.register_namespace('', NS)

DEBUG = False


def moveId(fileIn, fileOut):
    tree = ET.parse(fileIn)
    root = tree.getroot()
    didNoId = set()
    didMultId = set()
    didOneId = set()
    for cElem in root.iter('{{{}}}c'.format(NS)):
        for didElem in cElem.findall('{{{}}}did'.format(NS)):
            unitidElems = didElem.findall('{{{}}}unitid'.format(NS))
            ids = [u for u in unitidElems if u.attrib['type'] == TP]
            nIds = len(ids)
            if nIds == 0:
                didNoId.add(didElem)
            elif nIds == 1:
                didOneId.add(didElem)
            else:
                didMultId.add(didElem)
            if nIds == 0:
                unitidElem = ET.Element('unitid')
                unitidElem.text = cElem.attrib['id']
                unitidElem.set('label', EMI)
                unitidElem.set('encodinganalog', EA)
                didElem.insert(0, unitidElem)
    if DEBUG:
        print('{} => {}'.format(fileIn, fileOut))
        print('\t{:<4} x <did> with single identifier'.format(len(didOneId)))
        print('\t{:<4} x <did> without identifier'.format(len(didNoId)))
        print(
            '\t{:<4} x <did> with multiple identifiers'.format(len(didMultId))
        )
    tree.write(fileOut, encoding='unicode', xml_declaration=True)


moveId(*sys.argv[1:3])
