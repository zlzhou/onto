/*
 * File: app/store/ConTreeStore.js
 *
 * This file was generated by Sencha Architect version 2.2.2.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.2.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('MyApp.store.ConTreeStore', {
    extend: 'Ext.data.TreeStore',

    requires: [
        'MyApp.model.treemodel'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'MyApp.model.treemodel',
            storeId: 'ConTreeStore',
            root: {
                id: '1000000000',
                text: '概念'
            },
            proxy: {
                type: 'ajax',
                extraParams: {
                    text: 'initial',
                    id: '1000000000'
                },
                url: '/ontotest/dtest',
                reader: {
                    type: 'json',
                    root: 'menus'
                }
            },
            listeners: {
                beforeload: {
                    fn: me.onTreeStoreBeforeLoad,
                    scope: me
                },
                beforeexpand: {
                    fn: me.onTreeStoreBeforeExpand,
                    scope: me
                }
            }
        }, cfg)]);
    },

    onTreeStoreBeforeLoad: function(store, operation, eOpts) {
        //var n=Ext.getCmp('ConTreeView').getSelectionModel().getSelection();

        //alert(n[0].get('id'));
        //store.getProxy().setExtraParam('id',n[0].get('id'));
    },

    onTreeStoreBeforeExpand: function(nodeinterface, eOpts) {

        Ext.getCmp('ConTree').getStore().getProxy().setExtraParam('id',nodeinterface.get('id'));

    }

});